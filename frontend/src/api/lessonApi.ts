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

export const getAllLessons = async () => {
    const response = await fetch(`${backendUrl}/api/lessons/all/`, {
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

export const downloadFile = async (file: string) => {
    const response = await fetch(`${backendUrl}/api/storage/download/`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ objectName: file }),
        credentials: 'include',
        redirect: 'follow'
    });

    console.log("Response Status:", response.status);
    console.log("Response Content-Type:", response.headers.get("Content-Type"));

    if (!response.ok) {
        const errorText = await response.text();
        console.error("File download failed:", errorText);
        throw new Error(errorText);
    }

    const blob = await response.blob();
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement("a");

    const filename = file.split("/").pop() || "downloaded-file";
    a.href = url;
    a.download = filename;

    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    window.URL.revokeObjectURL(url);
};

