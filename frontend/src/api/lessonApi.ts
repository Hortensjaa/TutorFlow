const backendUrl = import.meta.env.VITE_BACKEND_URL || "http://localhost:8080";


export const addLesson = async (values: any) => {
    const formData = new FormData();
    const lesson = {
        id: -1,
        topic: values.lesson.topic,
        date: values.lesson.date,
        description: values.lesson.description,
        rate: values.lesson.rate,
        student: '',
        student_id: parseInt(values.lesson.student, 10),
        teacher: ''
    };

    formData.append('lesson', new Blob([JSON.stringify(lesson)], { type: 'application/json' }));

    if (values.newFiles) {
        values.newFiles.forEach((file: File) => formData.append('files', file));
    }

    const response = await fetch(`${backendUrl}/api/lessons/add/`, {
        method: 'POST',
        credentials: 'include',
        body: formData,
    });

    if (!response.ok) throw new Error("Failed to add lesson");

    return response.json();
};

export const editLesson = async (id: string, values: any) => {
    const formData = new FormData();
    const lesson = {
        id: parseInt(id || "-1"),
        topic: values.lesson.topic,
        date: values.lesson.date,
        description: values.lesson.description,
        rate: values.lesson.rate,
        student: '',
        student_id: parseInt(values.lesson.student, 10),
        teacher: '',
        files: values.lesson.files
    };

    formData.append('lesson', new Blob([JSON.stringify(lesson)], { type: 'application/json' }));

    if (values.newFiles) {
        values.newFiles.forEach((file: File) => formData.append('files', file));
    }

    const response = await fetch(`${backendUrl}/api/lessons/${id}/edit/`, {
        method: 'PUT',
        credentials: 'include',
        body: formData,
    });

    if (!response.ok) throw new Error("Failed to edit lesson");

    return response.json();
};

export const getLesson = async (id: string) => {
    const response = await fetch(`${backendUrl}/api/lessons/${id}/`, {
        redirect: "follow",
        credentials: "include",
    });

    if (!response.ok) throw new Error("Failed to fetch lesson");

    return response.json();
};

export const getAllLessons = async (page=0, size=20, sortBy=null, descending=true, studentId="") => {
    const response = await fetch(
        `${backendUrl}/api/lessons/all/?page=${page}&size=${size}&sortBy=${sortBy}&descending=${descending}&studentId=${studentId ? studentId : ""}`, {
        method: 'GET',
        redirect: 'follow',
        credentials: 'include',
    })
    if (!response.ok) throw new Error("Failed to fetch lessons");
    return response.json();
}

export const deleteLesson = async (id: string) => {
    const response = await fetch(`${backendUrl}/api/lessons/${id}/delete/`, {
        method: 'DELETE',
        credentials: 'include',
        redirect: 'follow',
    });

    if (!response.ok) throw new Error("Failed to delete lesson");

    return response;
};

export const getUpcomingLessons = async () => {
    const response = await fetch(
        `${backendUrl}/api/lessons/upcoming/`, {
            method: 'GET',
            credentials: 'include',
        })

    if (!response.ok) throw new Error("Failed to get latest lessons");

    return await response.json();
}



