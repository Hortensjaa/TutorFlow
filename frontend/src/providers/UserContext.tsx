import {User} from "../models";
import {createContext} from "react";


interface UserContext {
    state: User | null;
    loading: boolean;
    actions: {
        save: (newModel: User) => void;
        logout: () => void;
    };
}

export const UserContext = createContext<UserContext>({
    state: null,
    loading: true,
    actions: {
        save: (newModel: User) => {},
        logout: () => {}
    },
});