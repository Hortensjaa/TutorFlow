import { useContext, useEffect, useState } from "react";
import {
    Button, Divider, Dialog, Loader, ScrollArea, Text, TextInput, Title, Group
} from "@mantine/core";
import { SideNavbar } from "../index.ts";
import { TopNavbar } from "../navBar/TopNavbar.tsx";
import { useMediaQuery } from "@mantine/hooks";
import styles from './Profile.module.css';
import { useNavigate } from "react-router-dom";
import { UserContext } from "../../providers/UserContext.tsx";
import {TagsTable} from "./TagsTable.tsx";
import {EditStudentsTable} from "./EditStudentsTable.tsx";

const EditProfile = () => {
    const { state: thisUser, actions } = useContext(UserContext);
    const navigation = useNavigate();
    const isMobile = useMediaQuery('(max-width: 768px)');
    const [username, setUsername] = useState<string>("");

    useEffect(() => {
        if (thisUser) {
            setUsername(thisUser.username || "");
        }
    }, [thisUser]);

    const saveUser = async () => {
        if (thisUser) {
            await actions.save({
                ...thisUser,
                username: username,
            });
        }
        navigation("/profile");
    };

    return (
        <ScrollArea>
            <div className="container">
                {!isMobile ? <SideNavbar /> : <TopNavbar />}
                <div className="content">
                    <Title order={1} className={styles.title}>Profile Settings</Title>

                    <Divider my="md" label="Personal Data" labelPosition="center" />
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

                    <EditStudentsTable/>

                    <TagsTable/>

                    <div className="buttonContainer">
                        <Button onClick={saveUser} className="wideButton">
                            Save
                        </Button>
                    </div>
                </div>
            </div>
        </ScrollArea>
    );
};

export default EditProfile;
