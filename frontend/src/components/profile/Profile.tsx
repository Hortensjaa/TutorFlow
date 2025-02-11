import {UserContext} from "../../providers/UserContext.tsx";
import {useContext, useEffect, useState} from "react";
import {
    Box, Code,
    Divider,
    Loader, Table,
    Text,
    Title
} from "@mantine/core";
import {SideNavbar} from "../index.ts";
import {TopNavbar} from "../navBar/TopNavbar.tsx";
import {useMediaQuery} from "@mantine/hooks";
import styles from './Profile.module.css';
import {Lesson, Student} from "../../models";

const Profile = (locales?: Intl.LocalesArgument, options?: Intl.DateTimeFormatOptions) => {
    const { state: thisUser, actions } = useContext(UserContext)
    const isMobile = useMediaQuery('(max-width: 768px)');
    const [loading, setLoading] = useState<boolean>(true);
    const [latestLessons, setLessons] = useState<Lesson[]>([]);
    const [students, setStudents] = useState<Student[]>([]);

    useEffect(() => {
        const fetchUser = async () => {
            await actions.loadUser();
            setLoading(false);
        };
        fetchUser()
    }, []);

    useEffect(() => {
        fetch('/api/lessons/latest',
            {
                method: 'GET',
                redirect: 'follow',
                credentials: 'include'
            })
            .then(response => response.json())
            .then(data => {
                setLessons(data)
            })
    }, [])

    useEffect(() => {
        fetch('/api/user/students',
            {
                method: 'GET',
                redirect: 'follow',
                credentials: 'include'
            })
            .then(response => response.json())
            .then(data => {
                setStudents(data)
            })
    }, [])

    const lessonRows = latestLessons ? latestLessons.map((element: Lesson) => (
        <Table.Tr key={element.id}>
            <Table.Td>
                {new Date(element.date).toLocaleDateString(undefined, {
                    year: 'numeric',
                    month: 'numeric',
                    day: 'numeric',
                    weekday: 'short',
                })}
            </Table.Td>
            <Table.Td fw={500}>{element.topic}</Table.Td>
            <Table.Td>{element.student}</Table.Td>
        </Table.Tr>
    )) : null;

    const studentRows = students ? students.map((element: Student) => (
        <Table.Tr key={element.id}>
            <Table.Td>{element.name}</Table.Td>
            <Table.Td><Code> todo </Code></Table.Td>
            <Table.Td><Code> todo </Code></Table.Td>
        </Table.Tr>
    )) : null;

    return (
        <div className="container">
            {!isMobile ? <SideNavbar /> : <TopNavbar/>}
            {loading && (
                <div className={"loading"}>
                    <Loader type="bars" />
                </div>
            )}
            {!loading && (
                <div className="content">
                    <Title order={1} className={styles.title}>Profile</Title>

                    <Divider my="md" label="Personal data" labelPosition="center"/>
                    <Box className={styles.textContainer}>
                        <Text size="md" weight={500} c="dimmed" className={styles.label}>
                            Username
                        </Text>
                        <Text size="lg"  className={styles.value}>
                            {thisUser?.username || "N/A"}
                        </Text>
                    </Box>

                    <Box className={styles.textContainer}>
                        <Text size="md" weight={500} c="dimmed" className={styles.label}>
                            Email
                        </Text>
                        <Text size="lg" className={styles.value}>
                            {thisUser?.email || "N/A"}
                        </Text>
                    </Box>

                    <Divider my="md" label="Latest lessons" labelPosition="center"/>

                    {latestLessons.length > 0 ? (
                        <Table>
                            <Table.Thead>
                                <Table.Tr>
                                    <Table.Th className={styles.tableheader}>Date</Table.Th>
                                    <Table.Th className={styles.tableheader}>Topic</Table.Th>
                                    <Table.Th className={styles.tableheader}>Student</Table.Th>
                                </Table.Tr>
                            </Table.Thead>
                            <Table.Tbody>
                                {lessonRows}
                            </Table.Tbody>
                        </Table>
                    ) : (
                        <Text c="dimmed">
                            No lessons yet
                        </Text>
                    )}

                    <Divider my="md" label="My students" labelPosition="center"/>

                    {students.length > 0 ? (
                    <Table>
                        <Table.Thead>
                            <Table.Tr>
                                <Table.Th className={styles.tableheader}>Name</Table.Th>
                                <Table.Th className={styles.tableheader}>Last lesson date</Table.Th>
                                <Table.Th className={styles.tableheader}>Last lesson topic</Table.Th>
                            </Table.Tr>
                        </Table.Thead>
                        <Table.Tbody>
                            {studentRows}
                        </Table.Tbody>
                    </Table>) : (
                        <Text c="dimmed">
                            Add your first student in settings
                        </Text>
                    )}
                </div>
            )}
        </div>
    );
}

export default Profile
