import {UserContext} from "../../providers/UserContext.tsx";
import {useContext, useEffect, useState} from "react";
import {
    Badge,
    Box, Code,
    Divider,
    Loader,
    ScrollArea, Table,
    Text,
    Title
} from "@mantine/core";
import {SideNavbar} from "../index.ts";
import {TopNavbar} from "../navBar/TopNavbar.tsx";
import {useMediaQuery} from "@mantine/hooks";
import styles from './Profile.module.css';
import {Lesson} from "../../models";


const Profile = () => {
    const { state: thisUser, actions } = useContext(UserContext)
    const isMobile = useMediaQuery('(max-width: 768px)');
    const [loading, setLoading] = useState<boolean>(true);
    const [latestLessons, setLessons] = useState<Lesson[]>([]);

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
                console.log(data)
                setLessons(data)
            })
    }, [])

    const rows = latestLessons ? latestLessons.map((element: Lesson) => (
        <Table.Tr key={element.id}>
            <Table.Td>{element.date}</Table.Td>
            <Table.Td>{element.topic}</Table.Td>
            <Table.Td c={element.teacher == thisUser?.username ? "dimmed" : ""}>
                {element.teacher == thisUser?.username ? "You" : element.teacher}
            </Table.Td>
            <Table.Td c={element.student == thisUser?.username ? "dimmed" : ""}>
                {element.student == thisUser?.username ? "You" : element.student}
            </Table.Td>
        </Table.Tr>
    )) : null;

    return (
        <ScrollArea>
            <div className="container">
                {!isMobile ? <SideNavbar /> : <TopNavbar/>}
                {loading && (
                    <div className={styles.loading}>
                        <Loader type="bars" />
                    </div>
                )}
                {!loading && (
                    <div className="content">
                        <Box className={styles.titlebox}>
                            <Title order={1} className={styles.title}>Profile</Title>
                            <Box>
                                {thisUser?.teacher && (
                                    <Badge size="lg" radius="sm">Teacher</Badge>
                                )}
                                {thisUser?.teacher && (
                                    <Badge size="lg" radius="sm" ml="sm">Student</Badge>
                                )}
                            </Box>
                        </Box>

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
                        <Table>
                            <Table.Thead>
                                <Table.Tr>
                                    <Table.Th className={styles.tableheader}>Date</Table.Th>
                                    <Table.Th className={styles.tableheader}>Topic</Table.Th>
                                    <Table.Th className={styles.tableheader}>Teacher</Table.Th>
                                    <Table.Th className={styles.tableheader}>Student</Table.Th>
                                </Table.Tr>
                            </Table.Thead>
                            <Table.Tbody>{rows}</Table.Tbody>
                        </Table>

                        <Divider my="md" label="Coming lessons" labelPosition="center"/>
                        <Code>Not available yet</Code>
                    </div>
                )}
            </div>
        </ScrollArea>
    );
}

export default Profile
