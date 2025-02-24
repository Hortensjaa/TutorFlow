import {UserContext} from "../../providers/UserContext.tsx";
import {useContext, useEffect, useState} from "react";
import {
    Box,
    Divider,
    Loader,
    Text,
    Title
} from "@mantine/core";
import {SideNavbar} from "../index.ts";
import {TopNavbar} from "../navBar/TopNavbar.tsx";
import {useMediaQuery} from "@mantine/hooks";
import styles from './Profile.module.css';
import {Lesson, Student} from "../../models";
import {getStudents} from "../../api/studentApi.ts";
import {StudentsTable} from "./studentsTable.tsx";
import {getUpcomingLessons} from "../../api/lessonApi.ts";
import {UpcomingTable} from "./upcomingLessonsTable.tsx";

const Profile = () => {
    const { state: thisUser, _ } = useContext(UserContext)
    const isMobile = useMediaQuery('(max-width: 768px)');
    const [loading, setLoading] = useState({ students: true, lessons: true })

    const [students, setStudents] = useState<Student[]>([]);
    const [lessons, setLessons] = useState<Lesson[]>([])

    useEffect(() => {
        getStudents()
            .then((data) => {
                setStudents(data);
                setLoading((prevState) => ({ ...prevState, students: false }));
            })
            .catch(console.error);
    }, []);

    useEffect(() => {
        getUpcomingLessons()
            .then((data) => {
                setLessons(data);
                setLoading((prevState) => ({ ...prevState, lessons: false }));
            })
            .catch(console.error);
    }, []);


    return (
        <div className="container">
            {!isMobile ? <SideNavbar /> : <TopNavbar/>}
            {loading["lessons"] || loading["students"] && (
                <div className={"loading"}>
                    <Loader type="bars" />
                </div>
            )}
            {!loading["lessons"] && !loading["students"] && (
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
                        <StudentsTable students={students}/>
                    ) : (
                        <Text c="dimmed">
                            Add your first student in settings.
                        </Text>
                    )}

                    <Divider my="md" label="Upcoming lessons" labelPosition="center"/>
                    {lessons.length > 0 ? (
                        <UpcomingTable lessons={lessons}/>
                    ) : (
                        <Text c="dimmed">
                            Choose "Add lesson" from menu to plan a new lesson.
                        </Text>
                    )}

                </div>
            )}
        </div>
    );
}

export default Profile
