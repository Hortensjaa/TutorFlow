const backendUrl = import.meta.env.VITE_BACKEND_URL

export const getUserTags = async () => {
    const response = await fetch(`${backendUrl}/api/tags/all/`, {
        method: 'GET',
        credentials: 'include',
        headers: {
            "Content-Type": "application/json",
        }
    });

    if (!response.ok) throw new Error("Failed to load students");

    return await response.json();
}

export const addNewTag = async (name: String) => {
    const response = await fetch(`${backendUrl}/api/tags/add/`, {
        method: 'POST',
        credentials: 'include',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name })
    });
    if (!response.ok) throw new Error("Failed to add tag");

    return await response.json();
};