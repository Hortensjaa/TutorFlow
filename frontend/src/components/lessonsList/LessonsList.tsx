import { useEffect, useState } from 'react';
import { IconSearch } from '@tabler/icons-react';
import {
    ScrollArea,
    Table,
    Text,
    TextInput,
    useMantineTheme
} from '@mantine/core';

import { Lesson } from "../../models/lesson.ts";
import { sortData, Th } from "./utils.tsx";
import { SideNavbar } from "../index.ts";
import {TopNavbar} from "../navBar/TopNavbar.tsx";


export default function LessonsList() {
    const [search, setSearch] = useState('');
    const [sortedData, setSortedData] = useState<Lesson[]>([]);
    const [sortBy, setSortBy] = useState<keyof Lesson | null>(null);
    const [reverseSortDirection, setReverseSortDirection] = useState(false);
    const theme = useMantineTheme();

    const setSorting = (field: keyof Lesson) => {
        const reversed = field === sortBy ? !reverseSortDirection : false;
        setReverseSortDirection(reversed);
        setSortBy(field);
        setSortedData(sortData(sortedData, { sortBy: field, reversed, search }));
    };

    const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const { value } = event.currentTarget;
        setSearch(value);
        setSortedData(sortData(sortedData, { sortBy, reversed: reverseSortDirection, search: value }));
    };

    const rows = sortedData.map((row: Lesson) => (
        <Table.Tr key={row.id}>
            <Table.Td>{row.date}</Table.Td>
            <Table.Td>
                <Text fw={500}>
                    {row.topic}
                </Text>
            </Table.Td>
            <Table.Td>{row.student}</Table.Td>
        </Table.Tr>
    ));

    const loadData = () => {
        console.log("starting fetching data");
        fetch("/api/lessons",
            { method: 'GET', redirect: "follow", credentials: 'include' })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                console.log('Fetched Data:', data);
                setSortedData(data);
            })
            .catch(error => console.error('Fetch error:', error));
    }

    useEffect(() => {
        loadData();
    }, []);

    return (
        <ScrollArea>
            <div style={{ display: 'flex', flexDirection: 'row', width: '100%' }}>
                {parseFloat(theme.breakpoints.sm) * 16 < window.innerWidth ? <SideNavbar /> : null}

                <div style={{
                    display: 'flex',
                    flexDirection: 'column',
                    flex: 1,
                }}>
                    {parseFloat(theme.breakpoints.sm) * 16 < window.innerWidth ? null : <TopNavbar/>}
                    <TextInput
                        placeholder="Search by any field"
                        mb="md"
                        leftSection={<IconSearch />}
                        value={search}
                        onChange={handleSearchChange}
                        style={{
                            marginBottom: '10px',
                            marginLeft: parseFloat(theme.breakpoints.sm) * 16 > window.innerWidth ? '10px' : '0',
                            marginRight: parseFloat(theme.breakpoints.sm) * 16 > window.innerWidth ? '10px' : '0',}}
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
