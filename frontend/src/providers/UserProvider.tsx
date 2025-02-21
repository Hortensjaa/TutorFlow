import {useEffect, useState} from 'react';
import {UserContext} from "./UserContext.tsx";
import {User} from "../models";
import {useNavigate} from "react-router-dom";
import {getUser, logoutUser, saveUser} from "../api/userApi.ts";


export const UserProvider = ({ children }) => {
    const [user, setUser] = useState<User | null>(null);
    const [loading, setLoading] = useState(true);
    const backendUrl = import.meta.env.VITE_BACKEND_URL || 'http://localhost:8080';
    const navigate = useNavigate()

    useEffect(() => {
        const response = getUser();
        response.then((data) => {
            setUser(data);
        }).catch((error) => {
            setUser(null)
            console.log(error);
        }).finally(() => {
            setLoading(false);
        });
    }, []);


    const save = async (newModel: User) => {
        const response = saveUser(newModel);
        response.then((data) => {
            setUser(data);
        }).catch((error) => {
            console.log(error);
        }).finally(() => {
            setLoading(false);
        });
    };

    const logout = async () => {
        const response = logoutUser();
        response.then((data) => {
            setUser(null);
        }).catch((error) => {
            console.log(error);
        }).finally(() => {
            navigate('/')
        });
    }

    const value = {
        state: user,
        loading: loading,
        actions: { save, logout },
    };


    return (
        <UserContext.Provider value={value}>
            {children}
        </UserContext.Provider>
    )
}

