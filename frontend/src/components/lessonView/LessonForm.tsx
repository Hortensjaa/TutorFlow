import { useState, useEffect } from 'react';
import { useForm } from '@mantine/form';
import {
    TextInput,
    Select,
    Textarea, Input,
    Button, Title, Rating, Loader
} from '@mantine/core';
import '@mantine/dates/styles.css';
import { DateInput } from "@mantine/dates";
import { FileInput } from '@mantine/core';
import {Lesson, Student} from '../../models';
import styles from "./LessonView.module.css";

interface LessonFormProps {
    initialValues?: Lesson;
    onSubmit: (values: any) => Promise<void>;
    header: string;
}

const LessonForm = ({ initialValues, onSubmit, header }: LessonFormProps) => {
    const [students, setStudents] = useState<{ value: string; label: string }[]>([]);
    const [loading, setLoading] = useState<boolean>(true);

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
            topic: initialValues?.topic || '',
            date: initialValues?.date ? new Date(initialValues.date) : new Date(),
            rate: initialValues?.rate || 0,
            description: initialValues?.description || '',
            student: initialValues?.student_id?.toString() || '',
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

    return (
        <div className={"container"}>
            {loading && (
                <div className={"loading"}>
                    <Loader type="bars" />
                </div>
            )}
            {!loading && (
            <div className="content">
                <form onSubmit={form.onSubmit(onSubmit)} className={styles.formlesson}>
                    <Title order={1} className={styles.title} mb={"md"}>{header}</Title>

                    <TextInput
                        label="Topic"
                        placeholder="Enter lesson topic"
                        {...form.getInputProps('topic')}
                    />

                    <Input.Wrapper label="Rate">
                        <Rating
                            defaultValue={initialValues?.rate || 0}
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
                            {header}
                        </Button>
                    </div>
                </form>
            </div>
            )}
        </div>
    );
};

export default LessonForm;