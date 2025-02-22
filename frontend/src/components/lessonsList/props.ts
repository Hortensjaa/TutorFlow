import {Lesson} from "../../models";

export interface LessonRequestProps {
    lessons: Lesson[];
    sortBy: string;
    reverseSortDirection: boolean;
    setSortBy: (value: keyof Lesson) => void;
    setReverseSortDirection: (value: boolean) => void;
}