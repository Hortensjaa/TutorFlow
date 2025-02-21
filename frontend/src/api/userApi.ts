import {User} from "../models";

const backendUrl = import.meta.env.VITE_BACKEND_URL

export const getUser = async () => {
    const response = await fetch(`${backendUrl}/api/user/active/`, {
        method: "GET",
        credentials: "include",
        headers: {
            "Content-Type": "application/json"
        }
    });

    if (!response.ok) throw new Error("Failed to load user");

    return await response.json();
}

export const saveUser = async (newModel: User) => {
    const response = await fetch(`${backendUrl}/api/user/update/`, {
        method: "PUT",
        credentials: "include",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(newModel),
    });

    if (!response.ok) throw new Error("Failed to save user");

    return await response.json();
}

export const logoutUser = async () => {
    const response = await fetch(`${backendUrl}/logout`, {
        method: 'GET',
        credentials: 'include',
    });

    if (!response.ok) throw new Error("Failed to logout");

    return response;
}