import { useState, useEffect } from 'react';
import { useForm } from '@mantine/form';
import {
    TextInput,
    Select,
    Textarea, Input,
    Button, Title, Rating, Loader, InputBase, Pill, MultiSelect, Group, Box, Divider, UnstyledButton
} from '@mantine/core';
import '@mantine/dates/styles.css';
import { DateInput } from "@mantine/dates";
import { FileInput } from '@mantine/core';
import {Student, Tag} from '../../models';
import styles from "./LessonView.module.css";
import {trimPath} from "./utils.ts";
import {getStudents} from "../../api/studentApi.ts";
import {LessonFormProps} from "./props.ts";
import {addNewTag, getUserTags} from "../../api/tagsApi.ts";
import {IconPlus} from "@tabler/icons-react";


const LessonForm = ({ initialValues, onSubmit, header }: LessonFormProps) => {
    const [students, setStudents] = useState<{ value: string; label: string }[]>([]);
    const [tags, setTags] = useState<{ value: string; label: string }[]>([]);
    const [newTag, setNewTag] = useState<string>("");
    const [loading, setLoading] = useState({ students: true, tags: true })


    useEffect(() => {
        const response = getStudents();
        response.then((data) => {
            setStudents(data.map((student: Student) => ({
                value: student.id.toString(), label: student.name }
            )))})
            .catch((error) => console.error('Error fetching students:', error))
            .finally(() => setLoading((prevState) => ({ ...prevState, students: false })));
    }, []);

    useEffect(() => {
        const response = getUserTags();
        response.then((data) => {
            setTags(data.map((tag: Tag) => ({
                    value: tag.id.toString(), label: tag.name }
            )))})
            .catch((error) => console.error('Error fetching tags:', error))
            .finally(() => setLoading((prevState) => ({ ...prevState, tags: false })));
    }, []);

    const addTag = (tag: string) => {
        setNewTag("")
        const response = addNewTag(tag);
        response.then((data) => {
            setTags([...tags, { value: data.id.toString(), label: data.name }]);
            form.setFieldValue('lesson.tags', [...form.values.lesson.tags, data.id.toString()]);
        }).catch((error) => console.error('Error adding tag:', error));
    }

    const form = useForm({
        initialValues: {
            lesson: {
                topic: initialValues?.lesson.topic || '',
                date: initialValues?.lesson.date ? new Date(initialValues.lesson.date) : new Date(),
                rate: initialValues?.lesson.rate || 0,
                description: initialValues?.lesson.description || '',
                student: initialValues?.lesson.student_id?.toString() || '',
                files: initialValues?.lesson.files || [],
                tags: initialValues?.lesson.tags || [],
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
            {loading.tags || loading.students && (
                <div className={"loading"}>
                    <Loader type="bars" />
                </div>
            )}
            {!loading.tags && !loading.students &&
                (
                    <div className="content">
                        <form onSubmit={form.onSubmit(onSubmit)} className={styles.formlesson}>
                            <Title order={1} className={styles.title} mb={"md"}>{header}</Title>

                            <TextInput
                                label="Topic"
                                placeholder="Enter lesson topic"
                                {...form.getInputProps('lesson.topic')}
                            />

                            <Input.Wrapper label="Tags">
                                <Group spacing="xs" style={{ flexWrap: 'nowrap' }}>
                                    <MultiSelect
                                        data={tags}
                                        placeholder="Search..."
                                        searchable
                                        nothingFoundMessage="Nothing found..."
                                        style={{ flex: 1, minWidth: 0 }}
                                        onChange={(selectedTagIds) => {
                                            const selectedTags = selectedTagIds.map(tagId => {
                                                const tagObject = tags.find(tag => tag.value === tagId);
                                                return tagObject ? { id: Number(tagObject.value), name: tagObject.label } : null;
                                            }).filter(Boolean);

                                            form.setFieldValue('lesson.tags', selectedTags); // 🔹 Set transformed tags
                                        }}
                                    />

                                    <Divider orientation="vertical" />
                                    <TextInput
                                        value={newTag}
                                        onChange={(event) => setNewTag(event.currentTarget.value)}
                                        placeholder="Enter new tag"
                                        style={{ flex: 0.5, minWidth: 0 }}
                                    />
                                    <UnstyledButton
                                        onClick={() => addTag(newTag)}
                                        style={{flexShrink: 0, display: 'flex' }}
                                    >
                                        <IconPlus/>
                                    </UnstyledButton>

                                </Group>
                            </Input.Wrapper>

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
                    </div>
                )}
        </div>
    );
};

export default LessonForm;