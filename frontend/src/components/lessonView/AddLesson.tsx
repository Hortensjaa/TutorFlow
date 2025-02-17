import '@mantine/dates/styles.css';
import {useMediaQuery} from "@mantine/hooks";
import {useNavigate} from "react-router-dom";
import {Lesson} from '../../models';
import {SideNavbar} from "../index.ts";
import {TopNavbar} from "../navBar/TopNavbar.tsx";
import LessonForm from "./LessonForm.tsx";

const AddLesson = () => {
    const backendUrl = import.meta.env.VITE_BACKEND_URL || 'http://localhost:8080';
    const isMobile = useMediaQuery('(max-width: 768px)');
    const navigate = useNavigate();

    const handleSubmit = async (values: any) => {
        const formData = new FormData();
        let lesson: Lesson = {
            id: -1,
            topic: values.lesson.topic,
            date: values.lesson.date,
            description: values.lesson.description,
            rate: values.lesson.rate,
            student: '',
            student_id: parseInt(values.lesson.student, 10),
            teacher: ''
        }
        const lessonBlob = new Blob([JSON.stringify(lesson)], { type: 'application/json' });
        formData.append('lesson', lessonBlob);
        if (values.newFiles) {
            values.newFiles.forEach((file) => {
                formData.append('files', file);
            });
        }

        try {
            const response = await fetch(`${backendUrl}/api/lessons/add`, {
                method: 'POST',
                credentials: 'include',
                body: formData,
            });

            if (response.ok) {
                console.log('Lesson added successfully');
                await response.json();
                navigate("/dashboard");
            } else {
                console.error('Failed to add lesson:', response.statusText);
            }
        } catch (error) {
            console.error('Error adding lesson:', error);
        }
    };

    return (
        <div className={"container"}>
            {!isMobile ? <SideNavbar /> : <TopNavbar/>}
            <LessonForm
                onSubmit={handleSubmit}
                header="Add Lesson"
            />
        </div>
    );
};

export default AddLesson;