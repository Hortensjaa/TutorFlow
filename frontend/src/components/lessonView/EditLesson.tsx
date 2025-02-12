import '@mantine/dates/styles.css';
import {useMediaQuery} from "@mantine/hooks";
import {useNavigate, useParams} from "react-router-dom";
import {Lesson} from '../../models';
import {SideNavbar} from "../index.ts";
import {TopNavbar} from "../navBar/TopNavbar.tsx";
import LessonForm from "./LessonForm.tsx";
import {useEffect, useState} from "react";
import {Loader} from "@mantine/core";

const EditLesson = () => {
    const isMobile = useMediaQuery('(max-width: 768px)');
    const navigate = useNavigate();
    const [loading, setLoading] = useState<boolean>(true);
    const { id } = useParams();
    const [lesson, setLesson] = useState<Lesson | null>(null);

    useEffect(() => {
        const fetchLesson = async () => {
            setLoading(true);
            try {
                const response = await fetch(`/api/lessons/${id}`, {redirect: "follow"});
                const data = await response.json();
                setLesson(data);
            } catch (error) {
                console.error('Error fetching lesson:', error);
            } finally {
                setLoading(false);
            }
        }
        fetchLesson();
    }, [id]);

    const handleSubmit = async (values: any) => {
        const formData = new FormData();
        let lesson: Lesson = {
            id: parseInt(id || "-1"),
            topic: values.topic,
            date: values.date,
            description: values.description,
            rate: values.rate,
            student: '',
            student_id: parseInt(values.student, 10),
            teacher: ''
        }
        const lessonBlob = new Blob([JSON.stringify(lesson)], { type: 'application/json' });
        formData.append('lesson', lessonBlob);
        if (values.files) {
            values.files.forEach((file) => {
                formData.append('files', file);
            });
        }

        try {
            const response = await fetch(`/api/lessons/${id}/edit`, {
                method: 'PUT',
                credentials: 'include',
                body: formData,
            });

            if (response.ok) {
                console.log('Lesson edited successfully');
                await response.json();
                navigate(`/lesson/${id}`);
            } else {
                console.error('Failed to edit lesson:', response.statusText);
            }
        } catch (error) {
            console.error('Error edit lesson:', error);
        }
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
                initialValues={lesson || undefined}
                onSubmit={handleSubmit}
                header="Save Lesson"
            />
            )}
        </div>
    );
};

export default EditLesson;