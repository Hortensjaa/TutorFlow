import {UserContext} from "../../providers/UserContext.tsx";
import {useContext, useEffect, useState} from "react";
import {
    Box,
    Button,
    Divider,
    Loader,
    ScrollArea,
    Table,
    Text,
    TextInput,
    Title
} from "@mantine/core";
import {SideNavbar} from "../index.ts";
import {TopNavbar} from "../navBar/TopNavbar.tsx";
import {useMediaQuery} from "@mantine/hooks";
import styles from './Profile.module.css';
import {useNavigate} from "react-router-dom";
import {Student} from "../../models";
import {IconX} from "@tabler/icons-react";


const EditProfile = () => {
    const { state: thisUser, actions } = useContext(UserContext)
    const navigation = useNavigate();
    const isMobile = useMediaQuery('(max-width: 768px)');
    const [loading, setLoading] = useState<boolean>(true);
    const [username, setUsername] = useState<string>("");
    const [students, setStudents] = useState<Student[]>([]);
    const [newStudent, setNewStudent] = useState<string>("");

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
        fetch('/api/user/students',
            {
                method: 'GET',
                redirect: 'follow',
                credentials: 'include'
            })
            .then(response => response.json())
            .then(data => {
                setStudents(data)
            })
    }, [])

    const handleDelete = (student: Student) => {
        fetch(`/api/user/delete_student`, {
            method: 'DELETE',
            credentials: 'include',
            redirect: 'follow',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(student)
        })
            .then((response) => {
                if (response.ok) {
                    console.log('Student deleted successfully');
                    setStudents(students.filter((s) => s.id !== student.id));
                } else {
                    console.error('Failed to delete student');
                }
            })
            .catch((error) => console.error('Error deleting student:', error));
    };

    const addStudent = async () => {
        setNewStudent("")
        try {
            let response = await fetch('/api/user/add_student', {
                method: 'POST',
                credentials: 'include',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ name: newStudent }),
            });

            let data = await response.json();

            if (response.ok) {
                if (data.success) {
                    setStudents([...students, data.student]);
                } else {
                    alert(data.message);
                }
            } else {
                alert(data.message);
            }
        } catch (error) {
            console.error("Error adding student:", error);
            alert("A network error occurred. Please try again.");
        }
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
            <Table.Td onClick={(_) => handleDelete(element)}> <IconX/> </Table.Td>
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
                            <Button onClick={addStudent}
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
