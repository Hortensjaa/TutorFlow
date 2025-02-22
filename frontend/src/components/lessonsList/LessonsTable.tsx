import {
    IconChevronDown,
    IconChevronUp,
    IconSelector
} from '@tabler/icons-react';
import {
    Center,
    Group, Rating,
    Table,
    Text,
    UnstyledButton,
} from '@mantine/core';

import {Lesson} from "../../models";
import styles from './LessonsList.module.css';
import {useNavigate} from "react-router-dom";
import {useMediaQuery} from "@mantine/hooks";
import {LessonRequestProps} from "./props.ts";


function Th({ children, reversed, sorted, onSort }) {
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

export default function LessonsTable({lessons, sortBy, setSortBy, reverseSortDirection, setReverseSortDirection} : LessonRequestProps) {
    const navigate = useNavigate();
    const isMobile = useMediaQuery('(max-width: 768px)');

    const rows = lessons.map((row: Lesson) => (
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
        <Table horizontalSpacing="md" verticalSpacing="xs" miw={300} layout="fixed">
            <Table.Tbody>
                <Table.Tr>
                    <Th
                        sorted={sortBy === 'date'}
                        reversed={reverseSortDirection}
                        onSort={() => {
                            sortBy !== "date" ? setSortBy('date') : setReverseSortDirection(!reverseSortDirection)
                        }}
                    >
                        Date
                    </Th>
                    <Th
                        sorted={sortBy === 'topic'}
                        reversed={reverseSortDirection}
                        onSort={() => {
                            sortBy !== "topic" ? setSortBy('topic') : setReverseSortDirection(!reverseSortDirection)
                        }}
                    >
                        Topic
                    </Th>
                    <Th
                        sorted={sortBy === 'student'}
                        reversed={reverseSortDirection}
                        onSort={() => {
                            sortBy !== "student" ? setSortBy('student') : setReverseSortDirection(!reverseSortDirection)
                        }}
                    >
                        Student
                    </Th>
                    {
                        !isMobile &&
                        <Th
                            sorted={sortBy === 'rate'}
                            reversed={reverseSortDirection}
                            onSort={() => {
                                sortBy !== "rate" ? setSortBy('rate') : setReverseSortDirection(!reverseSortDirection)
                            }}
                        >
                            Overview
                        </Th>
                    }
                </Table.Tr>
            </Table.Tbody>
            <Table.Tbody>
                {rows}
            </Table.Tbody>
        </Table>
    );
}

