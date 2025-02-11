import {useState, useEffect} from 'react';
import { useForm } from '@mantine/form';
import {
    TextInput,
    Select,
    Textarea, Input,
    Button, Loader, Title, Rating
} from '@mantine/core';
import '@mantine/dates/styles.css';
import {useMediaQuery} from "@mantine/hooks";
import {DateInput} from "@mantine/dates";
import { FileInput } from '@mantine/core';
import {useNavigate} from "react-router-dom";
import {Lesson, Student} from '../../models';
import {SideNavbar} from "../index.ts";
import {TopNavbar} from "../navBar/TopNavbar.tsx";
import styles from "./LessonView.module.css";

const AddLesson = () => {
    const isMobile = useMediaQuery('(max-width: 768px)');
    const navigate = useNavigate()
    const [loading, setLoading] = useState<boolean>(true);
    const [students, setStudents] = useState<{ value: string; label: string }[]>([]);


    useEffect(() => {
        const fetchStudents = async () => {
            setLoading(true);
            try {
                const response = await fetch('/api/user/students',
                    {method: "GET", credentials: "include", redirect: "follow"} );
                const data = await response.json();
                setStudents(data.map((student: Student) => ({ value: student.id.toString(), label: student.name })));
            } catch (error) {
                console.error('Error fetching students:', error);
            } finally {
                setLoading(false);
            }
        };
        fetchStudents();
    }, []);


    const form = useForm({
        initialValues: {
            topic: '',
            date: new Date(),
            rate: 0,
            description: '',
            student: '',
            files: []
        },
        validate: {
            topic: (value) => (value.length < 3 ? 'Topic must be at least 3 characters' : null),
            date: (value) => (value === null ? 'Date is required' : null),
            student: (value) => (value === '' ? 'Student is required' : null),
            files: (value) => {
                if (value) {
                    for (const file of value) {
                        if (file.size > 10 * 1024 * 1024) {
                            return 'File size must be less than 10 MB';
                        }
                    }
                }
                return null;
            },
        },
    });

    const handleSubmit = async (values: typeof form.values) => {
        const formData = new FormData();
        let lesson: Lesson = {
            id: -1,
            topic: values.topic,
            date: values.date,
            description: values.description,
            rate: values.rate,
            student: students.find((s) => s.value === values.student)?.label || '',
            student_id: parseInt(values.student, 10),
            teacher: ''
        }
        console.log(lesson);
        const lessonBlob = new Blob([JSON.stringify(lesson)], { type: 'application/json' });
        formData.append('lesson', lessonBlob);
        if (values.files) {
            values.files.forEach((file) => {
                formData.append('files', file);
            });
        }

        try {
            const response = await fetch('/api/lessons/add', {
                method: 'POST',
                credentials: 'include',
                body: formData,
            });

            if (response.ok) {
                console.log('Lesson added successfully');
                const result = await response.json();
                console.log(result);
                navigate("/dashboard");
            } else {
                console.error('Failed to upload file:', response.statusText);
            }
        } catch (error) {
            console.error('Error uploading file:', error);
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
            <div className="content">
                <form onSubmit={form.onSubmit(handleSubmit)} className={styles.formlesson}>
                    <Title order={1} className={styles.title} mb={"md"}>Add new lesson</Title>

                    <TextInput
                        label="Topic"
                        placeholder="Enter lesson topic"
                        {...form.getInputProps('topic')}
                    />

                    <Input.Wrapper label="Rate">
                        <Rating
                            defaultValue={0}
                            onChange={(value) => form.setFieldValue('rate', value)} />
                    </Input.Wrapper>

                    <DateInput
                        valueFormat="DD MMM YYYY"
                        label="Date"
                        placeholder="Select lesson date"
                        {...form.getInputProps('date')}
                    />

                    <Textarea
                        label="Notes"
                        placeholder="Done excercises, homework, problems, overall reflection and preparation for next lesson"
                        {...form.getInputProps('description')}
                    />

                    <Select
                        label="Student"
                        placeholder="Select a student"
                        data={students}
                        {...form.getInputProps('student')}
                    />

                    <FileInput
                        clearable
                        label="Files"
                        placeholder="Upload files"
                        multiple
                        {...form.getInputProps('files')}
                    />

                    <div className="buttonContainer">
                        <Button type="submit" className="wideButton">
                            Add Lesson
                        </Button>
                    </div>
                </form>
            </div>
            )}
        </div>
    );
};

export default AddLesson;