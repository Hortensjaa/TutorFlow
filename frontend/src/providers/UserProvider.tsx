import {useState} from 'react';
import {UserContext} from "./UserContext.tsx";
import {User} from "../models";


export const UserProvider = ({ children }) => {
    const [user, setUser] = useState<User | null>(null);

    const loadUser = async () => {
        const response = await fetch('/api/user/active', {
            method: 'GET',
            credentials: 'include',
        });

        if (response.redirected) {
            document.location = response.url;
        }

        const data = await response.json();
        setUser(data);
    };

    function setName(newName: String) {
        if (user) {
            setUser({name: newName, ...user});
        }
    }

    function setAvatar(newUrl: String) {
        if (user) {
            setUser({avatar_url: newUrl, ...user});
        }
    }

    function setTeacher(val: boolean) {
        if (user) {
            setUser({teacher: val, ...user});
        }
    }

    function setStudent(val: boolean) {
        if (user) {
            setUser({student: val, ...user});
        }
    }

    const value = {
        state: user,
        actions: { setUser, loadUser, setName, setAvatar, setTeacher, setStudent },
    };


    return (
        <UserContext.Provider value={value}>
            {children}
        </UserContext.Provider>
    )
}

