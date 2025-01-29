import {UserContext} from "../../providers/UserContext.tsx";
import {useContext, useEffect, useState} from "react";
import {Box, Button, Checkbox, Divider, HoverCard, Loader, ScrollArea, Text, TextInput, Title} from "@mantine/core";
import {SideNavbar} from "../index.ts";
import {TopNavbar} from "../navBar/TopNavbar.tsx";
import {useMediaQuery} from "@mantine/hooks";
import styles from './Profile.module.css';


const EditProfile = () => {
    const { state: thisUser, actions } = useContext(UserContext)
    const isMobile = useMediaQuery('(max-width: 768px)');
    const [loading, setLoading] = useState<boolean>(true);
    const [username, setUsername] = useState<string>("");
    const [teacher, setTeacher] = useState<boolean>(false);
    const [student, setStudent] = useState<boolean>(false);

    useEffect(() => {
        const fetchUser = async () => {
            await actions.loadUser();
            setLoading(false);
        };
        fetchUser()
    }, []);

    useEffect(() => {
        if (thisUser) {
            setUsername(thisUser.username || "");
            setTeacher(thisUser.teacher || false);
            setStudent(thisUser.student || false);
        }
    }, [thisUser]);


    return (
        <ScrollArea>
            <div className={styles.container}>
                {!isMobile ? <SideNavbar /> : null}
                {loading && (<Loader type="bars" />)}
                {!loading && (
                    <div className={styles.content}>
                        {isMobile ? <TopNavbar/> : null}
                        <Title order={1} className={styles.title}>Profile settings</Title>

                        <Divider my="md" label="Personal data" labelPosition="center"/>
                        <TextInput
                            className={styles.textInput}
                            label="Username"
                            placeholder="Username"
                            value={username || ""}
                            onChange={(event) => setUsername(event.currentTarget.value)}
                        />
                        <TextInput
                            className={styles.textInput}
                            label="Email"
                            placeholder="email"
                            value={thisUser?.email}
                            disabled
                            mt="sm"
                        />

                        <HoverCard width={280} shadow="md">
                            <HoverCard.Target>
                                <Box>
                                    <Divider my="md" label="Your roles" labelPosition="center"/>
                                    <Checkbox
                                        className={styles.checkbox}
                                        label="Teacher"
                                        checked={teacher || false}
                                        onChange={() => setTeacher(!teacher)}
                                    />
                                    <Checkbox
                                        className={styles.checkbox}
                                        label="Student"
                                        checked={student || false}
                                        onChange={() => setStudent(!student)}
                                    />
                                </Box>
                            </HoverCard.Target>
                            <HoverCard.Dropdown>
                                <Text size="sm">
                                    Roles defines, what you can do on the platform.
                                    You can be both teacher and student and it is possible to change it anytime.
                                </Text>
                            </HoverCard.Dropdown>
                        </HoverCard>

                        <div className={styles.buttonContainer}>
                            <Button onClick={() => {}} className={styles.wideButton}>
                                Save
                            </Button>
                        </div>
                    </div>
                )}
            </div>
        </ScrollArea>
    );
}

export default EditProfile
