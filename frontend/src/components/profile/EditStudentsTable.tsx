import { useEffect, useState } from "react";
import {
    Box, Button, Divider, Dialog, Table, Text, TextInput, Group, Loader
} from "@mantine/core";
import { IconX } from "@tabler/icons-react";
import styles from './Profile.module.css';
import { Student } from "../../models";
import { addStudent, deleteStudent, getStudents } from "../../api/studentApi.ts";
import { useDisclosure } from "@mantine/hooks";

export const EditStudentsTable = () => {
    const [students, setStudents] = useState<Student[]>([]);
    const [newStudent, setNewStudent] = useState<string>("");
    const [opened, { toggle, close }] = useDisclosure(false);
    const [loading, setLoading] = useState(true);
    const [studentToDelete, setStudentToDelete] = useState<Student | null>(null);

    useEffect(() => {
        getStudents()
            .then(setStudents)
            .then(() => setLoading(false))
            .catch(console.error);
    }, []);

    const handleDeleteStudent = () => {
        if (studentToDelete) {
            deleteStudent(studentToDelete)
                .then(() => {
                    setStudents(students.filter((student) => student.id !== studentToDelete.id));
                    close();
                })
                .catch(console.error);
        }
    };

    const handleAddStudent = () => {
        if (newStudent) {
            addStudent(newStudent)
                .then((student) => setStudents([...students, student]))
                .catch(console.error);
        }
        setNewStudent("");
    };

    return (
        <>
            <Dialog opened={opened} withCloseButton onClose={close} size="lg" radius="md">
                <Text size="sm" mb="xs" fw={500}>
                    Are you sure you want to delete student {studentToDelete?.name}?
                    This action is irreversible and will happen immediately.
                </Text>
                <Group align="flex-end">
                    <Button onClick={handleDeleteStudent}>Delete</Button>
                </Group>
            </Dialog>

            <Divider mt="lg" mb="md" label="My Students" labelPosition="center" />
            <Box className={styles.boxadd}>
                <TextInput
                    flex={"0.7"}
                    placeholder="Name"
                    value={newStudent}
                    onChange={(event) => setNewStudent(event.currentTarget.value)}
                />
                <Button onClick={handleAddStudent} flex={"0.26"}>
                    Add Student
                </Button>
            </Box>

            {loading ? (
                <Text c="dimmed">Loading... </Text>
            ) : (
                students.length > 0 ? (
                    <Table>
                        <Table.Thead>
                            <Table.Tr>
                                <Table.Th className={styles.tableheader}>Name</Table.Th>
                                <Table.Th className={styles.tableheader}>Last Lesson Date</Table.Th>
                                <Table.Th className={styles.tableheader}>Delete</Table.Th>
                            </Table.Tr>
                        </Table.Thead>
                        <Table.Tbody>
                            {students.map((student) => (
                                    <Table.Tr key={student.id}>
                                        <Table.Td fw={500}>{student.name}</Table.Td>
                                        <Table.Td>
                                            {student.last_lesson ? new Date(student.last_lesson).toLocaleDateString() : null}
                                        </Table.Td>
                                        <Table.Td onClick={() => { setStudentToDelete(student); toggle(); }}>
                                            <IconX />
                                        </Table.Td>
                                    </Table.Tr>
                            ))}
                        </Table.Tbody>
                    </Table>
                    ) : (
                        <Text c="dimmed">Add your first student</Text>
                    )
                )
            }
        </>
    );
};

