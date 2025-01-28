import {UserContext} from "../../providers/UserContext.tsx";
import {useContext} from "react";
import {ScrollArea, Text, Title} from "@mantine/core";
import {SideNavbar} from "../index.ts";
import {TopNavbar} from "../navBar/TopNavbar.tsx";
import {useMediaQuery} from "@mantine/hooks";
import styles from './Profile.module.css';


const EditProfile = () => {
    const { state: thisUser, actions } = useContext(UserContext)
    const isMobile = useMediaQuery('(max-width: 768px)');

    return (
        <ScrollArea>
            <div className={styles.container}>
                {!isMobile ? <SideNavbar /> : null}

                <div className={styles.content}>
                    {isMobile ? <TopNavbar /> : null}
                    <Title order={1} style={{textAlign: 'center'}}>Profile</Title>
                    <Text> Username: {thisUser?.username} </Text>
                    <Text> Email: {thisUser?.email} </Text>
                    <Text> Teacher?: {thisUser?.teacher} </Text>
                    <Text> Student?: {thisUser?.student} </Text>
                </div>
            </div>
        </ScrollArea>
    )
}

export default EditProfile
