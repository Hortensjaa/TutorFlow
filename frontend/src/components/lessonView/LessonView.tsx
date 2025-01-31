import {useContext} from "react";
import {UserContext} from "../../providers/UserContext.tsx";
import {useMediaQuery} from "@mantine/hooks";


const LessonView = () => {
    const { state: thisUser, actions } = useContext(UserContext)
    const isMobile = useMediaQuery('(max-width: 768px)');
}