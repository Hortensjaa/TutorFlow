import {useEffect, useState} from 'react';
import {UserContext} from "./UserContext.tsx";
import {User} from "../models";
import {useNavigate} from "react-router-dom";
import {getUser, logoutUser, saveUser} from "../api/userApi.ts";
import {
    clearNotifications,
    editFailureNotification,
    editLoadingNotification,
    editSuccessNotification, logoutFailureNotification, logoutLoadingNotification, logoutSuccessNotification
} from "./notifications.tsx";


export const UserProvider = ({ children }) => {
    const [user, setUser] = useState<User | null>(null);
    const [loading, setLoading] = useState(true);
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
        editLoadingNotification();
        const response = saveUser(newModel);
        response.then((data) => {
            clearNotifications();
            editSuccessNotification();
            setUser(data);
        }).catch((error) => {
            clearNotifications();
            editFailureNotification();
            console.log(error);
        }).finally(() => {
            setLoading(false);
        });
    };

    const logout = async () => {
        logoutLoadingNotification();
        const response = logoutUser();
        response.then((_) => {
            clearNotifications();
            logoutSuccessNotification();
            setUser(null);
        }).catch((error) => {
            clearNotifications();
            logoutFailureNotification()
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

