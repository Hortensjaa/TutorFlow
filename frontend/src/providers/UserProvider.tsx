import {useEffect, useState} from 'react';
import {UserContext} from "./UserContext.tsx";
import {User} from "../models";
import {useNavigate} from "react-router-dom";


export const UserProvider = ({ children }) => {
    const [user, setUser] = useState<User | null>(null);
    const [loading, setLoading] = useState(true);
    const backendUrl = import.meta.env.VITE_BACKEND_URL || 'http://localhost:8080';
    const navigate = useNavigate()

    useEffect(() => {
        const fetchUser = async () => {
            try {
                const response = await fetch(`${backendUrl}/api/user/active/`, {
                    method: "GET",
                    credentials: "include",
                    headers: {
                        "Content-Type": "application/json",
                        "Accept": "application/json"
                    }
                });

                if (response.ok) {
                    const data = await response.json();
                    setUser(data);
                } else {
                    setUser(null);
                }
            } catch (error) {
                console.error("Error fetching user:", error);
                setUser(null);
            } finally {
                setLoading(false);
            }
        };

        fetchUser();
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
                setUser(updatedUser);
            } else {
                console.error("Failed to update user:", response.statusText);
            }
        } catch (error) {
            console.error("Error saving user:", error);
        }
    };

    const logout = async () => {
        try {
            await fetch(`${import.meta.env.VITE_BACKEND_URL}/logout`, {
                method: 'GET',
                credentials: 'include',
            });
            setUser(null);
            navigate('/')
        } catch (error) {
            console.error('Logout failed:', error);
        }
    }

    const value = {
        state: user,
        loading: loading,
        actions: { saveUser, logout },
    };


    return (
        <UserContext.Provider value={value}>
            {children}
        </UserContext.Provider>
    )
}

