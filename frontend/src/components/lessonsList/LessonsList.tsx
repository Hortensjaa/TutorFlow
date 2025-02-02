import { useEffect, useState } from 'react';
import {IconChevronDown, IconChevronUp, IconSearch, IconSelector} from '@tabler/icons-react';
import {
    Button,
    Center,
    Group,
    ScrollArea,
    Table,
    Text,
    TextInput, UnstyledButton,
    useMantineTheme
} from '@mantine/core';

import { Lesson } from "../../models";
import {sortData, ThProps} from "./utils.tsx";
import { SideNavbar } from "../index.ts";
import { TopNavbar } from "../navBar/TopNavbar.tsx";
import styles from './LessonsList.module.css';
import {useNavigate} from "react-router-dom";


export function Th({ children, reversed, sorted, onSort }: ThProps) {
    const Icon = sorted ? (reversed ? IconChevronUp : IconChevronDown) : IconSelector;
    return (
        <Table.Th className={styles.header}>
            <UnstyledButton onClick={onSort}>
                <Group justify="space-between">
                    <Text fw={500} fz="sm">
                        {children}
                    </Text>
                    <Center>
                        <Icon size={16} stroke={1.5} />
                    </Center>
                </Group>
            </UnstyledButton>
        </Table.Th>
    );
}

export default function LessonsList() {
    const navigate = useNavigate();
    const [search, setSearch] = useState('');
    const [originalData, setOriginalData] = useState<Lesson[]>([]);
    const [sortedData, setSortedData] = useState<Lesson[]>([]);
    const [sortBy, setSortBy] = useState<keyof Lesson | null>('date');
    const [reverseSortDirection, setReverseSortDirection] = useState(true);
    const theme = useMantineTheme();

    const setSorting = (field: keyof Lesson) => {
        const reversed = field === sortBy ? !reverseSortDirection : false;
        setReverseSortDirection(reversed);
        setSortBy(field);
        setSortedData(sortData(originalData, { sortBy: field, reversed, search }));
    };

    const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const { value } = event.currentTarget;
        setSearch(value);
        setSortedData(sortData(originalData, { sortBy, reversed: reverseSortDirection, search: value }));
    };

    const loadData = () => {
        console.log('starting fetching data');
        fetch('/api/lessons/all', {
            method: 'GET',
            redirect: 'follow',
            credentials: 'include',
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then((data) => {
                console.log('Fetched Data:', data);
                setOriginalData(data);
                setSortedData(sortData(data, { sortBy, reversed: true, search }));
            })
            .catch((error) => console.error('Fetch error:', error));
    };

    useEffect(() => {
        loadData();
    }, []);

    const rows = sortedData.map((row: Lesson) => (
        <Table.Tr key={row.id}>
            <Table.Td>{row.date}</Table.Td>
            <Table.Td>
                <Text fw={500}>{row.topic}</Text>
            </Table.Td>
            <Table.Td>{row.teacher}</Table.Td>
            <Table.Td>{row.student}</Table.Td>
        </Table.Tr>
    ));

    return (
        <ScrollArea>
            <div className={styles.container}>
                {parseFloat(theme.breakpoints.sm) * 16 < window.innerWidth ? <SideNavbar /> : null}

                <div className={styles.content}>
                    {parseFloat(theme.breakpoints.sm) * 16 < window.innerWidth ? null : <TopNavbar />}
                    <TextInput
                        placeholder="Search by any field"
                        mb="md"
                        leftSection={<IconSearch />}
                        value={search}
                        onChange={handleSearchChange}
                        className={styles.searchInput}
                    />
                    { rows.length !== 0 ? (
                    <Table horizontalSpacing="md" verticalSpacing="xs" miw={300} layout="fixed">
                        <Table.Tbody>
                            <Table.Tr>
                                <Th
                                    sorted={sortBy === 'date'}
                                    reversed={reverseSortDirection}
                                    onSort={() => setSorting('date')}
                                >
                                    Date
                                </Th>
                                <Th
                                    sorted={sortBy === 'topic'}
                                    reversed={reverseSortDirection}
                                    onSort={() => setSorting('topic')}
                                >
                                    Topic
                                </Th>
                                <Th
                                    sorted={sortBy === 'student'}
                                    reversed={reverseSortDirection}
                                    onSort={() => setSorting('student')}
                                >
                                    Student
                                </Th>
                                <Th
                                    sorted={sortBy === 'teacher'}
                                    reversed={reverseSortDirection}
                                    onSort={() => setSorting('teacher')}
                                >
                                    Teacher
                                </Th>
                            </Table.Tr>
                        </Table.Tbody>
                        <Table.Tbody>
                            {rows}
                        </Table.Tbody>
                    </Table>) : (
                        <div className={styles.buttoncontainer}>
                            <Button w={"50vw"} onClick={() => navigate("/lesson/add")}>
                                Add your first lesson!
                            </Button>
                        </div>
                    )}
                </div>
            </div>
        </ScrollArea>
    );
}

