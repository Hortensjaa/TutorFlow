import { useEffect, useState } from 'react';
import {IconChevronDown, IconChevronUp, IconSearch, IconSelector} from '@tabler/icons-react';
import {
    Center,
    Group,
    ScrollArea,
    Table,
    Text,
    TextInput, UnstyledButton,
    useMantineTheme
} from '@mantine/core';

import { Lesson } from "../../models/lesson.ts";
import {sortData, ThProps} from "./utils.tsx";
import { SideNavbar } from "../index.ts";
import { TopNavbar } from "../navBar/TopNavbar.tsx";
import styles from './LessonsList.module.css';


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
    const [search, setSearch] = useState('');
    const [originalData, setOriginalData] = useState<Lesson[]>([]); // Store the original data
    const [sortedData, setSortedData] = useState<Lesson[]>([]);
    const [sortBy, setSortBy] = useState<keyof Lesson | null>('date');
    const [reverseSortDirection, setReverseSortDirection] = useState(true);
    const theme = useMantineTheme();

    const setSorting = (field: keyof Lesson) => {
        const reversed = field === sortBy ? !reverseSortDirection : false;
        setReverseSortDirection(reversed);
        setSortBy(field);
        setSortedData(sortData(originalData, { sortBy: field, reversed, search })); // Use originalData
    };

    const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const { value } = event.currentTarget;
        setSearch(value);
        setSortedData(sortData(originalData, { sortBy, reversed: reverseSortDirection, search: value })); // Use originalData
    };

    const loadData = () => {
        console.log('starting fetching data');
        fetch('/api/lessons', {
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
                setOriginalData(data); // Store data in originalData
                setSortedData(sortData(data, { sortBy, reversed: true, search })); // Initialize sortedData
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
                            </Table.Tr>
                        </Table.Tbody>
                        <Table.Tbody>
                            {rows.length > 0 ? (
                                rows
                            ) : (
                                <Table.Tr>
                                    <Table.Td colSpan={4}>
                                        <Text fw={500} ta="center">
                                            Nothing found
                                        </Text>
                                    </Table.Td>
                                </Table.Tr>
                            )}
                        </Table.Tbody>
                    </Table>
                </div>
            </div>
        </ScrollArea>
    );
}

