import {MantineProvider} from "@mantine/core";
import {BrowserRouter as Router, Navigate, Outlet, Route, Routes} from "react-router-dom";

import {pinkTheme} from "./themes/colorTheme.tsx";
import {EditProfile, LessonsList, Login, Profile, AddLesson, LessonView, EditLesson, About} from "./components";
import {mantineCssVariableResolver} from "./themes/varResolver.tsx";
import HomePage from "./components/homePage/HomePage.tsx";
import {UserProvider} from "./providers/UserProvider.tsx";
import {Notifications} from "@mantine/notifications";
import "@mantine/notifications/styles.css";


const ProtectedRoutes = () => {
    return (
        <UserProvider>
            <Outlet />
        </UserProvider>
    );
};

function App() {
    return (
        <MantineProvider theme={pinkTheme} cssVariablesResolver={mantineCssVariableResolver}>
            <Notifications />
            <Router>
                <Routes>
                    <Route path="/" element={<HomePage />} />
                    <Route path="/login" element={<Login />} />
                    <Route path="/about" element={<About />} />

                    <Route element={<ProtectedRoutes />}>
                        <Route path="/dashboard" element={<LessonsList />} />
                        <Route path="/lesson/add" element={<AddLesson />} />
                        <Route path="/lesson/:id" element={<LessonView />} />
                        <Route path="/lesson/:id/edit" element={<EditLesson />} />
                        <Route path="/profile" element={<Profile />} />
                        <Route path="/profile/edit" element={<EditProfile />} />
                    </Route>

                    <Route path="*" element={<Navigate to="/" />} />
                </Routes>
            </Router>
        </MantineProvider>
    );
}

export default App
