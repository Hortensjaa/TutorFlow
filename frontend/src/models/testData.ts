import {Lesson} from "./lesson.ts";

const exampleLessons: Lesson[] = [
    {
        id: 1,
        topic: "Ciąg arytmetyczny - zadania",
        date: new Date("2025-01-24"),
        description: "aaaaaa",
        student: "Ewelina",
        teacher: "Jula"
    },
    {
        id: 2,
        topic: "Dziel i zwyciężaj - powtórzenie",
        date: new Date("2025-01-22"),
        description: "Potrzebne powtórzenie",
        student: "Kuba",
        teacher: "Ania"
    },
    {
        id: 3,
        topic: "Maturalne zadania z geometrii",
        date: new Date("2025-01-21"),
        description: "Zadanie z tangensem do powtórzenia!",
        student: "Filip",
        teacher: "Jula"
    },
]

export default exampleLessons