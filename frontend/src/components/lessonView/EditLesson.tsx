import '@mantine/dates/styles.css';
import {useMediaQuery} from "@mantine/hooks";
import {useNavigate, useParams} from "react-router-dom";
import {Lesson} from '../../models';
import {SideNavbar} from "../index.ts";
import {TopNavbar} from "../navBar/TopNavbar.tsx";
import LessonForm from "./LessonForm.tsx";
import {useEffect, useState} from "react";
import {Loader} from "@mantine/core";
import {editLesson, getLesson} from "../../api/lessonApi.ts";
import {
    clearNotifications,
    editFailureNotification,
    editLoadingNotification,
    editSuccessNotification
} from "./notifications.tsx";

const EditLesson = () => {
    const isMobile = useMediaQuery('(max-width: 768px)');
    const navigate = useNavigate();
    const [loading, setLoading] = useState<boolean>(true);
    const { id } = useParams();
    const [lesson, setLesson] = useState<Lesson | null>(null);

    useEffect(() => {
        const fetchLesson = async () => {
            try {
                const data = await getLesson(id as string);
                setLesson(data);
            } catch (error) {
                console.error("Error fetching lesson:", error);
            } finally {
                setLoading(false);
            }
        };
        if (id) fetchLesson();
    }, [id]);

    const handleEditLesson = async (values: any) => {
        editLoadingNotification();
        editLesson(id as String, values)
            .then(() => {
                clearNotifications();
                editSuccessNotification();
            })
            .catch((error) => {
                clearNotifications();
                editFailureNotification();
                console.error("Error editing lesson:", error);
            })
            .finally(() => navigate(`/lesson/${id}`))
    };

    return (
        <div className={"container"}>
            {!isMobile ? <SideNavbar /> : <TopNavbar/>}
            {loading && (
                <div className={"loading"}>
                    <Loader type="bars" />
                </div>
            )}
            {!loading && (
                <LessonForm
                    initialValues={lesson ? {lesson: lesson, newFiles: []} : undefined}
                    onSubmit={handleEditLesson}
                    header="Save Lesson"
                />
            )}
        </div>
    );
};

export default EditLesson;