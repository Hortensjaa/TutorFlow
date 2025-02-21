import {UserContext} from "../../providers/UserContext.tsx";
import {useContext, useEffect, useState} from "react";
import {
    Box,
    Divider,
    Loader, Table,
    Text,
    Title
} from "@mantine/core";
import {SideNavbar} from "../index.ts";
import {TopNavbar} from "../navBar/TopNavbar.tsx";
import {useMediaQuery} from "@mantine/hooks";
import styles from './Profile.module.css';
import { Student} from "../../models";
import {getStudents} from "../../api/studentApi.ts";
import {useNavigate} from "react-router-dom";

const Profile = () => {
    const { state: thisUser, actions } = useContext(UserContext)
    const isMobile = useMediaQuery('(max-width: 768px)');
    const [loading, setLoading] = useState<boolean>(true);
    const [students, setStudents] = useState<Student[]>([]);
    const navigate = useNavigate();

    useEffect(() => {
        getStudents()
            .then(setStudents)
            .then(() => setLoading(false))
            .catch(console.error);
    }, []);

    const NavLinkWrapper = ({ children, id }) => (
        <Table.Td
            style={{ cursor: id ? 'pointer' : 'default'}}
            onClick={() => id ? navigate(`/lesson/${id}`) : {}}
        >
            {children}
        </Table.Td>
    )

    const studentRows = students ? students.map((element: Student) => (
        <Table.Tr key={element.id}>
            <Table.Td fw={500}>{element.name}</Table.Td>
            <NavLinkWrapper id={element.last_lesson_id}>
                {element.last_lesson ? new Date(element.last_lesson).toLocaleDateString(undefined, {
                    year: 'numeric',
                    month: 'numeric',
                    day: 'numeric',
                }) : null}
            </NavLinkWrapper>
            <NavLinkWrapper id={element.last_lesson_id}>
                {element.last_topic}
            </NavLinkWrapper>
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
