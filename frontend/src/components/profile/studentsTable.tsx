import { Table } from "@mantine/core";
import { Student } from "../../models";
import styles from "./Profile.module.css";
import {useNavigate} from "react-router-dom";




const NavLinkWrapper = ({ children, id }) => {
    const navigate = useNavigate();

    return (
        <Table.Td
            style={{cursor: id ? "pointer" : "default"}}
            onClick={() => (id ? navigate(`/lesson/${id}`) : {})}
        >
            {children}
        </Table.Td>
    )
};

const studentRows = (students: Student[]) => {
    if (!students || students.length === 0) {
        return <Table.Tr><Table.Td colSpan={3}>No students found</Table.Td></Table.Tr>;
    }
    return students.map((element: Student) => (
        <Table.Tr key={element.id}>
            <Table.Td fw={500}>{element.name}</Table.Td>
            <NavLinkWrapper id={element.last_lesson_id}>
                {element.last_lesson
                    ? new Date(element.last_lesson).toLocaleDateString(undefined, {
                        year: "numeric",
                        month: "numeric",
                        day: "numeric",
                    })
                    : null}
            </NavLinkWrapper>
            <NavLinkWrapper id={element.last_lesson_id}>
                {element.last_topic}
            </NavLinkWrapper>
        </Table.Tr>
    ));
};


export const StudentsTable = ({ students }) => {
    return (
        <Table>
            <Table.Thead>
                <Table.Tr>
                    <Table.Th className={styles.tableheader}>Name</Table.Th>
                    <Table.Th className={styles.tableheader}>Last lesson date</Table.Th>
                    <Table.Th className={styles.tableheader}>Last lesson topic</Table.Th>
                </Table.Tr>
            </Table.Thead>
            <Table.Tbody>{studentRows(students)}</Table.Tbody>
        </Table>
    );
};

