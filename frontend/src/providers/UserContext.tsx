import {User} from "../models";
import {createContext} from "react";


interface UserContext {
    state: User | null;
    actions: {
        saveUser: (newModel: User) => void;
    };
}

export const UserContext = createContext<UserContext>({
    state: null,
    actions: {
        saveUser: (newModel: User) => {},
    },
});