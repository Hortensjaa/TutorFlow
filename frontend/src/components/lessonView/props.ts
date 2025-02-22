import {Lesson} from "../../models";

export interface LessonFormProps {
    initialValues?: { lesson: Lesson, newFiles: File[] };
    onSubmit: (values: any) => Promise<void>;
    header: string;
}