import {useMediaQuery} from "@mantine/hooks";
import {useNavigate} from "react-router-dom";
import {SideNavbar} from "../index.ts";
import {TopNavbar} from "../navBar/TopNavbar.tsx";
import LessonForm from "./LessonForm.tsx";
import {addLesson} from "../../api/lessonApi.ts";

const AddLesson = () => {
    const isMobile = useMediaQuery('(max-width: 768px)');
    const navigate = useNavigate();

    const handleSubmit = async (values: any) => {
        addLesson(values)
            .then(() => {
                console.log("Lesson added successfully");
            })
            .catch((error) => {
                console.error("Error adding lesson:", error);
            })
            .finally(() => {
                navigate("/dashboard")
            })
    };

    return (
        <div className={"container"}>
            {!isMobile ? <SideNavbar /> : <TopNavbar/>}
            <LessonForm
                onSubmit={handleSubmit}
                header="Add Lesson"
            />
        </div>
    );
};

export default AddLesson;