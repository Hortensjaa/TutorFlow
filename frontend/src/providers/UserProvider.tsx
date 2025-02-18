import {useState} from 'react';
import {UserContext} from "./UserContext.tsx";
import {User} from "../models";
import {useNavigate} from "react-router-dom";


export const UserProvider = ({ children }) => {
    const [user, setUser] = useState<User | null>(null);
    const backendUrl = import.meta.env.VITE_BACKEND_URL || 'http://localhost:8080';

    const loadUser = async (retry = false) => {
        try {
            const response = await fetch(`${backendUrl}/api/user/active/`, {
                method: 'GET',
                credentials: 'include',
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
                    window.location.href = `${backendUrl}/login`;  // Redirect manually
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


    const saveUser = async (newModel: User) => {
        const response = await fetch(`${backendUrl}/api/user/`, {
            method: 'PUT',
            credentials: 'include',
            redirect: 'follow',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(newModel),
        });

        if (response.redirected) {
            document.location = response.url;
        }
    };

    function setName(newName: String) {
        if (user) {
            setUser({name: newName, ...user});
        }
    }

    const value = {
        state: user,
        actions: { setUser, loadUser, setName, saveUser },
    };


    return (
        <UserContext.Provider value={value}>
            {children}
        </UserContext.Provider>
    )
}

