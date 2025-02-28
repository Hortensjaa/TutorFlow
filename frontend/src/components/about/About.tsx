import {useMediaQuery} from "@mantine/hooks";
import {SideNavbar} from "../index.ts";
import {TopNavbar} from "../navBar/TopNavbar.tsx";
import {Container, Title, Text, Code, Anchor, Group, Divider, List, Blockquote} from "@mantine/core";
import styles from "../lessonView/LessonView.module.css";
import { IconBrandGithub, IconButterfly } from "@tabler/icons-react";
import {ListItem} from "./ListItem.tsx";

const About = () => {
    const isMobile = useMediaQuery('(max-width: 768px)');

    return (
        <div className="container">
            {!isMobile ? <SideNavbar /> : <TopNavbar/>}
            <div className="content">
                <Title order={1} className={styles.title} mb="md" align="center">
                    About Tutor Flow
                </Title>

                <Group mb="md">
                    <IconBrandGithub size={16} />
                    <Code>
                          <span>
                          See source code on{' '}
                          <Anchor href="https://github.com/Hortensjaa/TutorFlow" target="_blank">
                            GitHub
                          </Anchor>.
                        </span>
                    </Code>
                </Group>

                <Text>
                    Tutor Flow is a web application designed for tutors who want to efficiently track their lessons and
                    monitor students' progress. The platform allows tutors to manage students, schedule and review
                    lessons, and store lesson-related files in an organized manner.
                </Text>

                <Blockquote cite="– Colleen Wilcox" my="xl" icon={<IconButterfly/>}>
                    Teaching is the greatest act of optimism.
                </Blockquote>

                <Title order={2} className={styles.title} mb="sm">
                    How to use?
                </Title>
                <List mb="md">
                    <List.Item>
                        <ListItem text="Go to" linkText="Settings" linkPath="/profile/edit" /> to add your students.
                    </List.Item>
                    <List.Item>
                        <ListItem text="Then choose" linkText="Add lesson" linkPath="/lesson/add" /> from the menu, to create a new lesson.
                    </List.Item>
                    <List.Item>
                        <ListItem text="You can see all your lessons on" linkText="Lessons" linkPath="/dashboard" /> dashboard.
                    </List.Item>
                    <List.Item>
                        <ListItem text="From dashboard you can view, edit or delete your lessons." />
                    </List.Item>
                    <List.Item>
                        <ListItem text="Add tags to better organise your lessons. You can always delete unused tags at " linkText="Settings" linkPath="/profile/edit" />
                    </List.Item>
                </List>

                <Divider m="xs" mt={"auto"}/>
                <Title order={4} className={styles.title} mb="md">
                    Contact
                </Title>
                <Text>
                    If you have any questions, ideas or any other feedback, feel free to contact me at{' '}
                    <Anchor href="mailto:lilka305@gmail.com">Gmail</Anchor>.
                    If you see any issues, please report it as a{' '}
                    <Anchor href="https://github.com/Hortensjaa/TutorFlow/issues">Github issue</Anchor>.
                </Text>


            </div>
        </div>
    );
};

export default About;