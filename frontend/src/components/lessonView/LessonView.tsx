import {useEffect, useState} from "react";
import {Lesson} from "../../models";
import {useNavigate, useParams} from "react-router-dom";
import {Button, Container, Group, Title, Text, Loader} from "@mantine/core";
import {SideNavbar} from "../index.ts";
import {TopNavbar} from "../navBar/TopNavbar.tsx";
import {useMediaQuery} from "@mantine/hooks";


const LessonView = () => {
    const isMobile = useMediaQuery('(max-width: 768px)');
    const { id } = useParams<{ id: string }>();
    const [loading, setLoading] = useState<boolean>(true);
    const navigate = useNavigate();
    const [lesson, setLesson] = useState<Lesson | null>(null);

    useEffect(() => {
        const fetchLesson = async () => {
            setLoading(true);
            try {
                const response = await fetch(`/api/lessons/${id}`);
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
        <div className={"container"}>
            {!isMobile ? <SideNavbar /> : <TopNavbar/>}
            {loading && (
                <div className={"loading"}>
                    <Loader type="bars" />
                </div>
            )}
            {!loading && (
            <Container className={"content"}>
                <Title order={2} mb="md">
                    Lesson Details
                </Title>

                <Group mb="sm">
                    <Text>Topic:</Text>
                    <Text>{lesson.topic}</Text>
                </Group>

                <Group mb="sm">
                    <Text>Date:</Text>
                    <Text>{new Date(lesson.date).toLocaleDateString(undefined,
                        {
                            year: 'numeric',
                            month: 'numeric',
                            day: 'numeric',
                            weekday: 'short',
                        })}
                    </Text>
                </Group>

                <Group mb="sm">
                    <Text>Description:</Text>
                    <Text>{lesson.description}</Text>
                </Group>

                <Group mb="sm">
                    <Text>Student:</Text>
                    <Text>{lesson.student}</Text>
                </Group>

                <Group c="dimmed" mb="sm">
                    <Text>Teacher:</Text>
                    <Text>{lesson.teacher}</Text>
                </Group>

                <div className="buttonContainer">
                    <Button onClick={handleDelete} variant={"outline"} className="wideButton">
                        Delete Lesson
                    </Button>
                </div>
            </Container>
            )}
        </div>
    );
};

export default LessonView;