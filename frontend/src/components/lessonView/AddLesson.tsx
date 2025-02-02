import {useState, useEffect} from 'react';
import { useForm } from '@mantine/form';
import {
    TextInput,
    Select,
    Textarea,
    Button,
    Box,
    Group,
} from '@mantine/core';
import {Lesson, User} from '../../models';
import {DateInput} from "@mantine/dates";
import {useNavigate} from "react-router-dom";

const AddLesson = () => {
    const navigate = useNavigate()
    const [students, setStudents] = useState<{ value: string; label: string }[]>([]);

    useEffect(() => {
        fetch('/api/user/students')
            .then((response) => response.json())
            .then((data) =>
                setStudents(data.map((student: User) => ({ value: student.id.toString(), label: student.username })))
            );
    }, []);


    const form = useForm({
        initialValues: {
            topic: '',
            date: new Date(),
            description: '',
            student: '',
        },
        validate: {
            topic: (value) => (value.length < 3 ? 'Topic must be at least 3 characters' : null),
            date: (value) => (value === null ? 'Date is required' : null),
            student: (value) => (value === '' ? 'Student is required' : null)
        },
    });

    const handleSubmit = async (values: typeof form.values) => {
        console.log('Form submitted:', values);
        let lesson: Lesson = {
            id: -1,
            topic: values.topic,
            date: values.date,
            description: values.description,
            student: students.find((s) => s.value === values.student)?.label || '',
            student_id: parseInt(values.student, 10),
            teacher: ''
        }
        await fetch('/api/lessons/add', {
            method: 'POST',
            credentials: 'include',
            redirect: 'follow',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(lesson),
        });
        navigate("/dashboard")
    };

    return (
        <Box maw={400} mx="auto">
            <form onSubmit={form.onSubmit(handleSubmit)}>
                <TextInput
                    label="Topic"
                    placeholder="Enter lesson topic"
                    {...form.getInputProps('topic')}
                />

                <DateInput
                    label="Date"
                    placeholder="Select lesson date"
                    {...form.getInputProps('date')}
                />

                <Textarea
                    label="Description"
                    placeholder="Enter lesson description"
                    {...form.getInputProps('description')}
                />

                <Select
                    label="Student"
                    placeholder="Select a student"
                    data={students}
                    {...form.getInputProps('student')}
                />

                <Group justify="flex-end" mt="md">
                    <Button type="submit">Add Lesson</Button>
                </Group>
            </form>
        </Box>
    );
};

export default AddLesson;