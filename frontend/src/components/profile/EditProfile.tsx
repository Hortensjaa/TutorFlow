import {UserContext} from "../../providers/UserContext.tsx";
import {useContext, useEffect, useState} from "react";
import {
    Box,
    Button,
    Divider, Dialog,
    Loader,
    ScrollArea,
    Table,
    Text,
    TextInput,
    Title, Group
} from "@mantine/core";
import {SideNavbar} from "../index.ts";
import {TopNavbar} from "../navBar/TopNavbar.tsx";
import {useDisclosure, useMediaQuery} from "@mantine/hooks";
import styles from './Profile.module.css';
import {useNavigate} from "react-router-dom";
import {Student} from "../../models";
import {IconX} from "@tabler/icons-react";
import {addStudent, deleteStudent, getStudents} from "../../api/userApi.ts";


const EditProfile = () => {
    const { state: thisUser, actions } = useContext(UserContext)
    const navigation = useNavigate();
    const isMobile = useMediaQuery('(max-width: 768px)');
    const [loading, setLoading] = useState<boolean>(true);
    const [username, setUsername] = useState<string>("");
    const [students, setStudents] = useState<Student[]>([]);
    const [newStudent, setNewStudent] = useState<string>("");
    const [opened, { toggle, close }] = useDisclosure(false);
    const [studentToDelete, setStudentToDelete] = useState<Student | null>(null);

    useEffect(() => {
        const fetchUser = async () => {
            await actions.loadUser();
            setLoading(false);
        };
        fetchUser()
    }, []);

    useEffect(() => {
        if (thisUser) {
            setUsername(thisUser.username || "");
        }
    }, [thisUser]);

    useEffect(() => {
        getStudents()
            .then(setStudents)
            .catch(console.error);
    }, [])

    const handleDelete = () => {
        if (studentToDelete) {
            deleteStudent(studentToDelete)
                .then((id) => {
                    console.log(id)
                    console.log(students)
                    setStudents(students.filter((student) => student.id !== id))
                })
                .catch((error) => console.error('Error deleting student:', error));
        }
    };

    const handleAdd = () => {
        if (newStudent) {
            addStudent(newStudent)
                .then((student) => setStudents([...students, student]))
                .catch((error) => console.error('Error adding student:', error));
        }
        setNewStudent("")
    };

    const studentRows = students ? students.map((element: Student) => (
        <Table.Tr key={element.id}>
            <Table.Td fw={500}>{element.name}</Table.Td>
            <Table.Td>
                {element.last_lesson ? new Date(element.last_lesson).toLocaleDateString(undefined, {
                    year: 'numeric',
                    month: 'numeric',
                    day: 'numeric',
                }) : null}
            </Table.Td>
            <Table.Td onClick={(_) => {
                setStudentToDelete(element);
                toggle()
            }}> <IconX/> </Table.Td>
        </Table.Tr>
    )) : null;

    const saveUser = async () => {
        console.log(thisUser)
        if (thisUser) {
            actions.setName(username);
            await actions.saveUser({
                ...thisUser,
                username: username,
            })
        }
        navigation("/profile")
    }


    return (
        <ScrollArea>
            <div className={"container"}>
                {!isMobile ? <SideNavbar /> : <TopNavbar/>}
                {loading && (
                    <div className={"loading"}>
                        <Loader type="bars" />
                    </div>
                )}
                {!loading && (
                    <div className={"content"}>
                        <Dialog opened={opened} withCloseButton onClose={close} size="lg" radius="md">
                            <Text size="sm" mb="xs" fw={500}>
                                Are you sure you want to delete student {studentToDelete?.name}?
                                This action cannot be reverted and will be save immediately.
                            </Text>

                            <Group align="flex-end">
                                <Button onClick={(_) => {
                                    handleDelete()
                                    close()
                                }}>Delete</Button>
                            </Group>
                        </Dialog>

                        <Title order={1} className={styles.title}>Profile settings</Title>

                        <Divider my="md" label="Personal data" labelPosition="center"/>
                        <TextInput
                            className={styles.textInput}
                            label="Username"
                            placeholder="Username"
                            value={username || ""}
                            onChange={(event) => setUsername(event.currentTarget.value)}
                        />
                        <TextInput
                            className={styles.textInput}
                            label="Email"
                            placeholder="email"
                            value={thisUser?.email}
                            disabled
                            mt="sm"
                        />

                        <Divider mt="lg" mb="md" label="My students" labelPosition="center"/>
                        <Box className={styles.boxadd}>
                            <TextInput
                                flex={"0.7"}
                                placeholder="name"
                                value={newStudent}
                                onChange={(event) => setNewStudent(event.currentTarget.value)}
                            />
                            <Button onClick={handleAdd}
                                    flex={"0.26"}>
                                Add student
                            </Button>
                        </Box>
                        {students.length > 0 ? (
                            <Table>
                                <Table.Thead>
                                    <Table.Tr>
                                        <Table.Th className={styles.tableheader}>Name</Table.Th>
                                        <Table.Th className={styles.tableheader}>Last lesson date</Table.Th>
                                        <Table.Th className={styles.tableheader}>Delete</Table.Th>
                                    </Table.Tr>
                                </Table.Thead>
                                <Table.Tbody>
                                    {studentRows}
                                </Table.Tbody>
                            </Table>) : (
                            <Text c="dimmed">
                                Add your first student
                            </Text>
                        )}
                        <div className="buttonContainer">
                            <Button onClick={saveUser} className="wideButton">
                                Save
                            </Button>
                        </div>
                    </div>
                )}
            </div>
        </ScrollArea>
    );
}

export default EditProfile
