import {Box, Button, Container, Dialog, Grid, Group, Text, Title} from '@mantine/core';
import {
    IconBook,
    IconUsers,
    IconTrendingUp,
    IconSparkles,
    IconCalendarEvent,
    IconBooks,
    IconArrowBigRight
} from '@tabler/icons-react';
import {useDisclosure, useMediaQuery} from '@mantine/hooks';
import { motion } from 'framer-motion';
import { useNavigate } from "react-router-dom";
import Login from "./Login.tsx";

interface elemProps {
    id: number, color: number, text: string, icon: any
}

const Element = (item: elemProps) => {
    const isMobile = useMediaQuery('(max-width: 768px)');

    return (
        <Grid.Col span={4}>
            <motion.div
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                transition={{ delay: 1 + item.id/4, duration: 0.5 }}
                style={{ textAlign: 'center', margin: isMobile? '0 0' : '0 50px' }}
            >
                <item.icon size={64} color={`var(--mantine-primary-color-${item.color})`} />
                <Title order={4} style={{
                    color: `var(--mantine-primary-color-${item.color})`,
                }}>
                    {item.text}
                </Title>
            </motion.div>
        </Grid.Col>
    )
}

export default function HomePage() {
    const navigate = useNavigate();
    const isMobile = useMediaQuery('(max-width: 768px)');
    const [opened, { toggle, close }] = useDisclosure(false);

    return (
        <Container size="lg" py="xl" h="100vh"
                   style={{ height: '100vh', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>

            <Dialog
                position={{ top: 20, right: 20 }}
                opened={opened}
                withCloseButton
                onClose={close}
                size="lg" radius="md"
            >
                <Login/>
            </Dialog>

            <Box
                sx={{
                    textAlign: 'center',
                    padding: '20px',
                    alignItems: 'center',
                }}
            >
                <motion.div
                    initial={{ opacity: 0, y: -20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.5 }}
                >
                    <Box
                        style={{width: "100%", display: "flex" ,justifyContent: 'center', alignItems: "center"}}>
                        <Box style={{width: "100%", maxWidth: "900px"}}>
                            <Title order={1} size={isMobile ? "50px" : "90px"} align="left" mb="md">
                                Welcome to{' '}
                            </Title>
                            <Title order={1} size={isMobile ? "60px" : "110px"} align="right" mb="md">
                                <Text
                                    span
                                    variant="gradient"
                                    gradient={{ from: 'orange', to: 'var(--mantine-primary-color-7)', deg: 180 }}
                                    inherit>
                                    Tutor Flow
                                </Text>
                            </Title>
                        </Box>
                    </Box>

                    <Text align="center" size="lg">
                        Organize your teaching process, track your students' progress, and manage lessons with ease.
                    </Text>
                </motion.div>
                <Grid mt={"lg"} w={isMobile ? "90vw" : "75vw"}>
                    <Element id={1} color={3} text={"Find inspiration"} icon={IconSparkles} />
                    <Element id={3} color={5} text={"Plan lessons"} icon={IconBook} />
                    <Element id={5} color={7} text={"Stay on schedule"} icon={IconCalendarEvent} />
                    <Element id={6} color={8} text={"Manage students"} icon={IconUsers} />
                    <Element id={4} color={6} text={"Organize materials"} icon={IconBooks} />
                    <Element id={2} color={4} text={"Track progress"} icon={IconTrendingUp} />
                </Grid>
                <motion.div
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ delay: 1.5, duration: 1.0 }}
                >
                    <Group position="center" mt="xl"
                           style={{width: "100%", display: "flex" ,justifyContent: 'center', alignItems: "center"}}>
                        <Button
                            w="50vw"
                            radius={"100px"}
                            variant={"gradient"}
                            leftSection={isMobile ? null : <IconArrowBigRight style={{margin: '0 10px'}}/>}
                            rightSection={isMobile ? null : <IconArrowBigRight style={{margin: '0 10px'}}/>}
                            gradient={{ from: 'var(--mantine-primary-color-7)', to: 'var(--mantine-primary-color-9)', deg: 135 }}
                            onClick={isMobile ? () => navigate('/login') : toggle}
                        >
                            GET STARTED
                        </Button>
                    </Group>
                </motion.div>
            </Box>
        </Container>
    );
}

