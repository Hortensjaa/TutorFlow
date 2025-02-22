import { useState, useEffect } from 'react';
import { useForm } from '@mantine/form';
import {
    TextInput,
    Select,
    Textarea, Input,
    Button, Title, Rating, Loader, InputBase, Pill
} from '@mantine/core';
import '@mantine/dates/styles.css';
import { DateInput } from "@mantine/dates";
import { FileInput } from '@mantine/core';
import {Lesson, Student} from '../../models';
import styles from "./LessonView.module.css";
import {trimPath} from "./utils.ts";
import {getStudents} from "../../api/studentApi.ts";
import {LessonFormProps} from "./props.ts";


const LessonForm = ({ initialValues, onSubmit, header }: LessonFormProps) => {
    const [students, setStudents] = useState<{ value: string; label: string }[]>([]);
    const [loading, setLoading] = useState<boolean>(true);


    useEffect(() => {
        setLoading(true);
        const response = getStudents();
        response.then((data) => {
            setStudents(data.map((student: Student) => ({
                value: student.id.toString(), label: student.name }
            )))})
            .catch((error) => console.error('Error fetching students:', error))
            .finally(() => setLoading(false));
    }, []);

    const form = useForm({
        initialValues: {
            lesson: {
                topic: initialValues?.lesson.topic || '',
                date: initialValues?.lesson.date ? new Date(initialValues.lesson.date) : new Date(),
                rate: initialValues?.lesson.rate || 0,
                description: initialValues?.lesson.description || '',
                student: initialValues?.lesson.student_id?.toString() || '',
                files: initialValues?.lesson.files || [],
            },
            newFiles: [],
        },
        validate: {
            lesson: {
                topic: (value) => (value.length < 3 ? 'Topic must be at least 3 characters' : null),
                date: (value) => (value === null ? 'Date is required' : null),
                student: (value) => (value === '' ? 'Student is required' : null)
            },
            newFiles: (value) => {
                if (value) {
                    for (const file of value) {
                        if (file.size > 10 * 1024 * 1024) {
                            return 'File size must be less than 10 MB';
                        }
                    }
                }
                return null;
            }
        },
    });

    return (
        <div className={"container"}>
            {loading && (
                <div className={"loading"}>
                    <Loader type="bars" />
                </div>
            )}
            {!loading && <div className="content">
                <form onSubmit={form.onSubmit(onSubmit)} className={styles.formlesson}>
                    <Title order={1} className={styles.title} mb={"md"}>{header}</Title>

                    <TextInput
                        label="Topic"
                        placeholder="Enter lesson topic"
                        {...form.getInputProps('lesson.topic')}
                    />

                    <Input.Wrapper label="Overview">
                        <Rating
                            defaultValue={initialValues?.lesson.rate || 0}
                            onChange={(value) => form.setFieldValue('lesson.rate', value)} />
                    </Input.Wrapper>

                    <DateInput
                        valueFormat="DD MMM YYYY"
                        label="Date"
                        placeholder="Select lesson date"
                        {...form.getInputProps('lesson.date')}
                    />

                    <Textarea
                        label="Notes"
                        placeholder="Done excercises, homework, problems, overall reflection and preparation for next lesson"
                        {...form.getInputProps('lesson.description')}
                    />

                    <Select
                        label="Student"
                        placeholder="Select a student"
                        data={students}
                        {...form.getInputProps('lesson.student')}
                    />

                    {
                        form.values.lesson.files.length > 0 && (
                            <InputBase component="div" label={"Previously uploaded files"} multiline>
                                <Pill.Group>
                                    {
                                        form.values.lesson.files.map((file: String, index: number) => (
                                            <Pill
                                                key={index}
                                                withRemoveButton
                                                onRemove={() => form.setFieldValue('lesson.files',
                                                    form.values.lesson.files.filter((_, i) => i !== index))}
                                            >
                                                {trimPath(file)}
                                            </Pill>
                                        ))
                                    }
                                </Pill.Group>
                            </InputBase>
                        )
                    }


                    <FileInput
                        clearable
                        label="Files"
                        placeholder="Upload new files"
                        multiple
                        {...form.getInputProps('newFiles')}
                    />

                    <div className="buttonContainer">
                        <Button type="submit" className="wideButton">
                            {header}
                        </Button>
                    </div>
                </form>
            </div>}
        </div>
    );
};

export default LessonForm;