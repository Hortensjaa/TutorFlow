import {User} from "../models";
import {createContext} from "react";


interface UserContext {
    state: User | null;
    actions: {
        setUser: (value: (((prevState: (User | null)) => (User | null)) | User | null)) => void;
        loadUser: () => void;
        saveUser: (newModel: User) => void;
        setName: (name: string) => void;
        setAvatar: (avatarUrl: string) => void;
        setTeacher: (role: boolean) => void;
        setStudent: (role: boolean) => void;
    };
}

export const UserContext = createContext<UserContext>({
    state: null,
    actions: {
        setUser: () => {},
        loadUser: () => {},
        saveUser: (newModel: User) => {},
        setName: () => {},
        setAvatar: () => {},
        setTeacher: () => {},
        setStudent: () => {},
    },
});