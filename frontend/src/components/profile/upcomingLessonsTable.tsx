import {useNavigate} from "react-router-dom";
import {Table} from "@mantine/core";
import {Lesson} from "../../models";
import styles from "./Profile.module.css";

const NavLinkWrapper = ({ children, id, bold }) => {
    const navigate = useNavigate();

    return (
        <Table.Td
            fw={bold ? 500 : 400}
            style={{cursor: id ? "pointer" : "default"}}
            onClick={() => (id ? navigate(`/lesson/${id}`) : {})}
        >
            {children}
        </Table.Td>
    )
};

const lessonRows = (lessons: Lesson[]) => {
    if (!lessons || lessons.length === 0) {
        return <Table.Tr><Table.Td colSpan={3}>No students found</Table.Td></Table.Tr>;
    }
    return lessons.map((element: Lesson) => (
        <Table.Tr key={element.id}>
            <NavLinkWrapper id={element.id}>
                {element.date
                    ? new Date(element.date).toLocaleDateString(undefined, {
                        year: "numeric",
                        month: "numeric",
                        day: "numeric",
                    })
                    : null}
            </NavLinkWrapper>
            <NavLinkWrapper bold={true} id={element.id}>
                {element.topic}
            </NavLinkWrapper>
            <Table.Td>{element.student}</Table.Td>
        </Table.Tr>
    ));
};


export const UpcomingTable = ({ lessons }) => {
    return (
        <Table>
            <Table.Thead>
                <Table.Tr>
                    <Table.Th className={styles.tableheader}>Date</Table.Th>
                    <Table.Th className={styles.tableheader}>Topic</Table.Th>
                    <Table.Th className={styles.tableheader}>Student</Table.Th>
                </Table.Tr>
            </Table.Thead>
            <Table.Tbody>{lessonRows(lessons)}</Table.Tbody>
        </Table>
    );
};
