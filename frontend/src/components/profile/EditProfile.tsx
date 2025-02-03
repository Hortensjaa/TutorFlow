import {UserContext} from "../../providers/UserContext.tsx";
import {useContext, useEffect, useState} from "react";
import {Box, Button, Checkbox, Divider, HoverCard, Loader, ScrollArea, Text, TextInput, Title} from "@mantine/core";
import {SideNavbar} from "../index.ts";
import {TopNavbar} from "../navBar/TopNavbar.tsx";
import {useMediaQuery} from "@mantine/hooks";
import styles from './Profile.module.css';
import {useNavigate} from "react-router-dom";


const EditProfile = () => {
    const { state: thisUser, actions } = useContext(UserContext)
    const navigation = useNavigate();
    const isMobile = useMediaQuery('(max-width: 768px)');
    const [loading, setLoading] = useState<boolean>(true);
    const [username, setUsername] = useState<string>("");

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
        }
    }, [thisUser]);

    const saveUser = async () => {
        console.log(thisUser)
        if (thisUser) {
            actions.setName(username);
            await actions.saveUser({
                ...thisUser,
                username: username,
            })
        }
        navigation("/profile")
    }


    return (
        <ScrollArea>
            <div className={"container"}>
                {!isMobile ? <SideNavbar /> : <TopNavbar/>}
                {loading && (
                    <div className={"loading"}>
                        <Loader type="bars" />
                    </div>
                )}
                {!loading && (
                    <div className={"content"}>
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
                        <div className="buttonContainer">
                            <Button onClick={saveUser} className="wideButton">
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
