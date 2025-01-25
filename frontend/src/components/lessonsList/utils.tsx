import {ReactNode} from 'react';
import {Lesson} from "../../models/lesson.ts";


export interface ThProps {
    children: ReactNode;
    reversed: boolean;
    sorted: boolean;
    onSort: () => void;
}

export function filterData(data: Lesson[], search: string) {
    const query = search.toLowerCase().trim();
    return data.filter((item) =>
        item.topic.toLowerCase().includes(query)
        || item.description.toLowerCase().includes(query)
        || item.student.toLowerCase().includes(query)
    );
}

export function sortData(
    data: Lesson[],
    payload: { sortBy: keyof Lesson | null; reversed: boolean; search: string }
) {
    const { sortBy, reversed, search } = payload;

    if (!sortBy) {
        return filterData(data, search);
    }

    return filterData(
        [...data].sort((a, b) => {
            const valA = a[sortBy];
            const valB = b[sortBy];

            if (typeof valA === "string" && typeof valB === "string") {
                return reversed ? valB.localeCompare(valA) : valA.localeCompare(valB);
            }

            if (typeof valA === "number" && typeof valB === "number") {
                return reversed ? valB - valA : valA - valB;
            }

            if (valA instanceof Date && valB instanceof Date) {
                return reversed ? valB.getTime() - valA.getTime() : valA.getTime() - valB.getTime();
            }

            return 0;
        }),
        search
    );
}