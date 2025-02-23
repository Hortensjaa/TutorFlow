const backendUrl = import.meta.env.VITE_BACKEND_URL || "http://localhost:8080";

export const downloadFile = async (file: string) => {
    const response = await fetch(`${backendUrl}/api/storage/download/`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ objectName: file }),
        credentials: 'include',
        redirect: 'follow'
    });
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