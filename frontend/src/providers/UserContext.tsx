import {User} from "../models";
import {createContext} from "react";


interface UserContext {
    state: User | null;
    loading: boolean;
    actions: {
        saveUser: (newModel: User) => void;
        logout: () => void;
    };
}

export const UserContext = createContext<UserContext>({
    state: null,
    loading: true,
    actions: {
        saveUser: (newModel: User) => {},
        logout: () => {}
    },
});