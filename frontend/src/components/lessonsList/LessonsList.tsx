import { useEffect, useState } from 'react';
import {
    IconCaretLeftFilled,
    IconCaretRightFilled, IconSearch,
} from '@tabler/icons-react';
import {
    Button, Loader, Text,
    ScrollArea, Select, useMantineTheme, TextInput, Checkbox, Box, Group,
} from '@mantine/core';

import {Lesson, Student} from "../../models";
import { SideNavbar } from "../index.ts";
import { TopNavbar } from "../navBar/TopNavbar.tsx";
import styles from './LessonsList.module.css';
import {useNavigate} from "react-router-dom";
import {useMediaQuery} from "@mantine/hooks";
import {getAllLessons} from "../../api/lessonApi.ts";
import {getStudents} from "../../api/studentApi.ts";
import LessonsTable from "./LessonsTable.tsx";


export default function LessonsList() {
    const theme = useMantineTheme();
    const navigate = useNavigate();
    const isMobile = useMediaQuery('(max-width: 768px)');
    const [loading, setLoading] = useState(true);

    const [lessons, setLessons] = useState<Lesson[]>([]);
    const [allLessons, setAllLessons] = useState<Lesson[]>([]);
    const [totalPages, setTotalPages] = useState(1);
    const [search, setSearch] = useState("");
    const [searchBy, setSearchBy] = useState({topic: true, tag: true, description: false});

    const [students, setStudents] = useState<{ value: string; label: string }[]>([]);
    const [studentId, setStudentId] = useState("");

    const [page, setPage] = useState(0);
    const [size, setSize] = useState(25);
    const [sortBy, setSortBy] = useState<keyof Lesson>('date');
    const [reverseSortDirection, setReverseSortDirection] = useState(true);

    useEffect(() => {
        setLoading(true);
        const response = getStudents();
        response.then((data) => {
            setStudents(data.map((student: Student) => ({
                    value: student.id.toString(), label: student.name }
            )))})
            .catch((error) => console.error('Error fetching students:', error))
            .finally(() => setLoading(false));
    }, []);

    useEffect(() => {
        setLoading(true);
        getAllLessons(page, size, sortBy, reverseSortDirection, studentId)
            .then((data) => {
                setLessons(data["content"]);
                setAllLessons(data["content"])
                setTotalPages(data["totalPages"])
            })
            .catch((error) => console.error('Fetch error:', error))
            .finally(() => setLoading(false));
    }, [page, size, sortBy, reverseSortDirection, studentId]);

    const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const value = event.target.value;
        setSearch(value);

        if (value.trim() === "") {
            setLessons(allLessons);
            return;
        }

        setLessons(allLessons.filter((lesson) =>
            (searchBy.topic && lesson.topic.toLowerCase().includes(value.toLowerCase()))
            || (searchBy.tag && lesson.tags.some((tag) => tag.name.toLowerCase().includes(value.toLowerCase())))
            || (searchBy.description && lesson.description.toLowerCase().includes(value.toLowerCase()))
        ));
    };

    return (
        <div className={styles.container}>
            {!isMobile ? <SideNavbar/> : null}
            {loading && (
                <div className={"loading"}>
                    <Loader type="bars"/>
                </div>
            )}

            {!loading && (
                <div className={styles.content}>
                    {!isMobile ? null : <TopNavbar/>}
                    {size !== 0 ? (
                        <div>
                            <Box style={{ display: "flex", alignItems: "center", gap: "1rem", flexWrap: "wrap"}}>
                                <TextInput
                                    placeholder="Search..."
                                    mb="md"
                                    leftSection={<IconSearch />}
                                    value={search}
                                    onChange={handleSearchChange}
                                    className={styles.searchInput}
                                    style={{ flex: 1 }}
                                />
                                <Group spacing="sm">
                                    <Checkbox
                                        label="Topic"
                                        checked={searchBy.topic}
                                        onChange={() => setSearchBy(prev => ({ ...prev, topic: !prev.topic }))}
                                    />
                                    <Checkbox
                                        label="Tag"
                                        checked={searchBy.tag}
                                        onChange={() => setSearchBy(prev => ({ ...prev, tag: !prev.tag }))}
                                    />
                                    <Checkbox
                                        label="Notes"
                                        checked={searchBy.description}
                                        onChange={() => setSearchBy(prev => ({ ...prev, description: !prev.description }))}
                                    />
                                </Group>
                            </Box>


                            <Select
                                data={students}
                                placeholder={"Filter by student"}
                                onChange={(value) => setStudentId(value)}
                                value={studentId}
                                clearable
                                mb={10}
                            />
                            <LessonsTable
                                lessons={lessons}
                                sortBy={sortBy}
                                reverseSortDirection={reverseSortDirection}
                                setSortBy={setSortBy}
                                setReverseSortDirection={setReverseSortDirection}
                            />
                            <div style={{display: "flex", alignItems: "center", justifyContent: "space-between"}}>
                                <div style={{display: "flex", alignItems: "center", gap: "8px"}}>
                                    <IconCaretLeftFilled
                                        onClick={() => page > 0 ? setPage(page - 1) : null}
                                        style={{color: page === 0 ? "gray" : theme.colors[theme.primaryColor][6]}}
                                    />
                                    <span> Page {page + 1} of {totalPages} </span>
                                    <IconCaretRightFilled
                                        onClick={() => page < totalPages - 1 ? setPage(page + 1) : null}
                                        style={{color: page < totalPages - 1 ? theme.colors[theme.primaryColor][6] : "gray"}}
                                    />
                                </div>

                                <div style={{display: "flex", alignItems: "center", gap: "8px"}}>
                                    <Text>Lessons per page:{' '}</Text>
                                    <Select
                                        data={["5", "10", "25", "50", "100"].map((value) => ({value: value, label: value}))}
                                        onChange={(value) => {
                                            setSize(value as number);
                                            setPage(0);
                                        }}
                                        value={size}
                                        w={75}
                                    />
                                </div>
                            </div>
                        </div>
                    ) : (
                        <div className={styles.buttoncontainer}>
                            <Button w={"50vw"} onClick={() => navigate("/lesson/add")}>
                                Add your first lesson!
                            </Button>
                        </div>
                    )}
                </div>
            )}
        </div>
    );
}

