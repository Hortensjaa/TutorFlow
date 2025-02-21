import {Student} from "../models";

const backendUrl = import.meta.env.VITE_BACKEND_URL

export const getStudents = async () => {
    const response = await fetch(`${backendUrl}/api/students/all/`, {
        method: 'GET',
        credentials: 'include',
        headers: {
            "Content-Type": "application/json",
        }
    });

    if (!response.ok) throw new Error("Failed to load students");

    return await response.json();
}

export const deleteStudent = async (student: Student): Promise<number> => {
    const response = await fetch(`${backendUrl}/api/students/delete/`, {
        method: 'DELETE',
        credentials: 'include',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(student)
    });

    if (!response.ok) throw new Error("Failed to delete student");

    return student.id;
};


export const addStudent = async (name: String) => {
    const response = await fetch(`${backendUrl}/api/students/add/`, {
        method: 'POST',
        credentials: 'include',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name })
    });

    const data = await response.json();

    if (!response.ok || !data.success) throw new Error(data.message || "Failed to add student");

    return data.student;
};