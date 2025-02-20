import {useEffect, useState} from 'react';
import {UserContext} from "./UserContext.tsx";
import {User} from "../models";


export const UserProvider = ({ children }) => {
    const [user, setUser] = useState<User | null>(null);
    const backendUrl = import.meta.env.VITE_BACKEND_URL || 'http://localhost:8080';

    useEffect(() => {
        const loadUser = async (retry = false) => {
            try {
                const response = await fetch(`${backendUrl}/api/user/active/`, {
                    method: "GET",
                    credentials: "include",
                    headers: {
                        "Content-Type": "application/json",
                        "Accept": "application/json"
                    }
                });

                if (response.status === 401) {
                    console.warn("🚨 User is not authenticated! Redirecting...");
                    if (!retry) {
                        console.log("🔄 Retrying after 1 second...");
                        setTimeout(() => loadUser(true), 1000);
                    } else {
                        window.location.href = `${backendUrl}/login`; // Redirect manually
                    }
                    return;
                }

                const data = await response.json();
                console.log("✅ User loaded:", data);
                setUser(data);
            } catch (error) {
                console.error("❌ Error loading user:", error);
            }
        };

        loadUser();
    }, []);


    const saveUser = async (newModel: User) => {
        try {
            const response = await fetch(`${backendUrl}/api/user/`, {
                method: "PUT",
                credentials: "include",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(newModel),
            });

            if (response.ok) {
                const updatedUser = await response.json();
                setUser(updatedUser); // ✅ Update frontend state
            } else {
                console.error("Failed to update user:", response.statusText);
            }
        } catch (error) {
            console.error("Error saving user:", error);
        }
    };

    const value = {
        state: user,
        actions: { saveUser },
    };


    return (
        <UserContext.Provider value={value}>
            {children}
        </UserContext.Provider>
    )
}

