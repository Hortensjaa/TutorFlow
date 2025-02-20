import { useEffect, useState } from 'react';
import {IconChevronDown, IconChevronUp, IconSearch, IconSelector} from '@tabler/icons-react';
import {
    Button,
    Center,
    Group, Loader, Rating,
    ScrollArea,
    Table,
    Text,
    TextInput, UnstyledButton,
} from '@mantine/core';

import { Lesson } from "../../models";
import {sortData, ThProps} from "./utils.tsx";
import { SideNavbar } from "../index.ts";
import { TopNavbar } from "../navBar/TopNavbar.tsx";
import styles from './LessonsList.module.css';
import {useNavigate} from "react-router-dom";
import {useMediaQuery} from "@mantine/hooks";
import {getAllLessons} from "../../api/lessonApi.ts";


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
    const backendUrl = import.meta.env.VITE_BACKEND_URL || 'http://localhost:8080';
    const navigate = useNavigate();
    const isMobile = useMediaQuery('(max-width: 768px)');
    const [search, setSearch] = useState('');
    const [originalData, setOriginalData] = useState<Lesson[]>([]);
    const [sortedData, setSortedData] = useState<Lesson[]>([]);
    const [sortBy, setSortBy] = useState<keyof Lesson | null>('date');
    const [reverseSortDirection, setReverseSortDirection] = useState(true);
    const [loading, setLoading] = useState(true);

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

    useEffect(() => {
        setLoading(true);
        getAllLessons()
            .then((data) => {
                setOriginalData(data);
                setSortedData(sortData(data, { sortBy, reversed: true, search }));
            })
            .catch((error) => console.error('Fetch error:', error))
            .finally(() => setLoading(false));
    }, []);

    const rows = sortedData.map((row: Lesson) => (
        <Table.Tr style={{cursor: "pointer"}} key={row.id} onClick={() => navigate(`/lesson/${row.id}`)}>
            {!isMobile ?
            <Table.Td>
                {new Date(row.date).toLocaleDateString(undefined, {
                    year: 'numeric',
                    month: 'numeric',
                    day: 'numeric',
                    weekday: 'short',
                })}
            </Table.Td>
                :
                <Table.Td>
                    {new Date(row.date).toLocaleDateString(undefined, {
                        year: 'numeric',
                        month: 'numeric',
                        day: 'numeric'
                    })}
                </Table.Td>
            }
            <Table.Td>
                <Text fw={500}>{row.topic}</Text>
            </Table.Td>
            <Table.Td>{row.student}</Table.Td>
            {
                !isMobile && <Table.Td><Rating value={row.rate} readOnly /></Table.Td>
            }
        </Table.Tr>
    ));

    return (
        <ScrollArea>
            <div className={styles.container}>
                {!isMobile ? <SideNavbar /> : null}
                {loading && (
                    <div className={"loading"}>
                        <Loader type="bars" />
                    </div>
                )}

                {!loading && (
                    <div className={styles.content}>
                        {!isMobile ? null : <TopNavbar/>}
                        <TextInput
                            placeholder="Search by topic or student's name"
                            mb="md"
                            leftSection={<IconSearch/>}
                            value={search}
                            onChange={handleSearchChange}
                            className={styles.searchInput}
                        />
                        {rows.length !== 0 ? (
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
                                        {
                                            !isMobile &&
                                            <Th
                                                sorted={sortBy === 'rate'}
                                                reversed={reverseSortDirection}
                                                onSort={() => setSorting('rate')}
                                            >
                                                Overview
                                            </Th>
                                        }
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
                )}

            </div>
        </ScrollArea>
    );
}

