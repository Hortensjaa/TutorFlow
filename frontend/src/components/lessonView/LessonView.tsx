import {useEffect, useState} from "react";
import {Lesson} from "../../models";
import {useNavigate, useParams} from "react-router-dom";
import {Button, Card, Container, Group, Title, Text} from "@mantine/core";


const LessonView = () => {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();
    const [lesson, setLesson] = useState<Lesson | null>(null);

    useEffect(() => {
        fetch(`/api/lessons/${id}`)
            .then((response) => response.json())
            .then((data) => setLesson(data))
            .catch((error) => console.error('Error fetching lesson:', error));
    }, [id]);

    const handleDelete = () => {
        fetch(`/api/lessons/${id}/delete`, {
            method: 'DELETE',
            credentials: 'include',
            redirect: 'follow',
        })
            .then((response) => {
                if (response.ok) {
                    console.log('Lesson deleted successfully');
                    navigate('/dashboard');
                } else {
                    console.error('Failed to delete lesson');
                }
            })
            .catch((error) => console.error('Error deleting lesson:', error));
    };

    if (!lesson) {
        return <div>Loading...</div>;
    }

    return (
        <Container size="sm" mt="xl">
            <Card shadow="sm" padding="lg">
                <Title order={2} mb="md">
                    Lesson Details
                </Title>

                <Group mb="sm">
                    <Text>Topic:</Text>
                    <Text>{lesson.topic}</Text>
                </Group>

                <Group mb="sm">
                    <Text>Date:</Text>
                    <Text>{new Date(lesson.date).toLocaleDateString()}</Text>
                </Group>

                <Group mb="sm">
                    <Text>Description:</Text>
                    <Text>{lesson.description}</Text>
                </Group>

                <Group mb="sm">
                    <Text>Student:</Text>
                    <Text>{lesson.student}</Text>
                </Group>

                <Group mb="sm">
                    <Text>Teacher:</Text>
                    <Text>{lesson.teacher}</Text>
                </Group>

                <Button onClick={handleDelete} variant={"outline"}>
                    Delete Lesson
                </Button>
            </Card>
        </Container>
    );
};

export default LessonView;