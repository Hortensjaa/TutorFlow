import {useEffect, useState} from "react";
import {Lesson} from "../../models";
import {useNavigate, useParams} from "react-router-dom";
import {Button, Container, Dialog, Group, Loader, Pill, Rating, Text, Title} from "@mantine/core";
import {SideNavbar} from "../index.ts";
import {TopNavbar} from "../navBar/TopNavbar.tsx";
import {useDisclosure, useMediaQuery} from "@mantine/hooks";
import styles from "./LessonView.module.css"
import {trimPath} from "./utils.ts";


const LessonView = () => {
    const backendUrl = import.meta.env.VITE_BACKEND_URL || 'http://localhost:8080';
    const isMobile = useMediaQuery('(max-width: 768px)');
    const { id } = useParams<{ id: string }>();
    const [loading, setLoading] = useState<boolean>(true);
    const navigate = useNavigate();
    const [lesson, setLesson] = useState<Lesson | null>(null);
    const [opened, { toggle, close }] = useDisclosure(false);

    useEffect(() => {
        const fetchLesson = async () => {
            setLoading(true);
            try {
                const response = await fetch(`${backendUrl}/api/lessons/${id}`, {redirect: "follow", credentials: "include"});
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
        fetch(`${backendUrl}/api/lessons/${id}/delete`, {
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

    const handleDownload = (file) => {
        console.log('Downloading file:', file);
        fetch(`${backendUrl}/api/storage/download`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ objectName: file }),
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('File download failed');
                }
                return response.blob();
            })
            .then(blob => {
                const url = window.URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.href = url;
                a.download = trimPath(file);
                document.body.appendChild(a);
                a.click();
                document.body.removeChild(a);
                window.URL.revokeObjectURL(url);
            })
            .catch(error => {
                console.error('Error downloading file:', error);
            });
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
            <Container className={"content"}>
                <Dialog opened={opened} withCloseButton onClose={close} size="lg" radius="md">
                    <Text size="sm" mb="xs" fw={500}>
                        Are you sure you want to delete this lesson? This action cannot be reverted.
                    </Text>

                    <Group align="flex-end">
                        <Button onClick={(_) => {
                            handleDelete();
                            close()
                        }}>Delete</Button>
                    </Group>
                </Dialog>

                <Text c="dimmed" mb="md">
                    Lesson Details
                </Text>

                <Group mb="sm">
                    <Title order={2} className={styles.title}>{lesson?.topic}</Title>
                </Group>

                <Group mb="sm">
                    <Text c="dimmed">Student:</Text>
                    <Text>{lesson?.student}</Text>
                </Group>

                <Group mb="sm">
                    <Text c="dimmed">Date:</Text>
                    <Text>{new Date(lesson?.date).toLocaleDateString(undefined,
                        {
                            year: 'numeric',
                            month: 'numeric',
                            day: 'numeric',
                            weekday: 'short',
                        })}
                    </Text>
                </Group>

                <Group mb="sm">
                    <Text c="dimmed">Rate:</Text>
                    <Rating value={lesson?.rate} readOnly />
                </Group>

                <Group mb="sm">
                    <Text c="dimmed">Notes:</Text>
                    <Text>{lesson?.description}</Text>
                </Group>

                <Group>
                    <Text c="dimmed">Files:</Text>
                    {lesson?.files?.length > 0 &&
                        lesson?.files?.map((file) => (
                            <Pill key={file}
                                  onClick={() => handleDownload(file)}
                                  style={{cursor: "pointer"}}
                            >
                                {trimPath(file)}
                            </Pill>
                        ))
                    }
                </Group>


                <div className="buttonContainer">
                    <Button
                        onClick={() => navigate(`/lesson/${id}/edit`)}
                        variant={"outline"}
                        c="green"
                        color="green"
                        className="wideButton"
                    >
                        Edit Lesson
                    </Button>
                    <Button onClick={toggle} variant={"outline"} className="wideButton" ml={"10px"}>
                        Delete Lesson
                    </Button>
                </div>
            </Container>
            )}
        </div>
    );
};

export default LessonView;