import {MantineProvider} from "@mantine/core";
import {BrowserRouter as Router, Navigate, Route, Routes} from "react-router-dom";

import {pinkTheme} from "./themes/colorTheme.tsx";
import {EditProfile, LessonsList, Login, Profile, AddLesson, LessonView, EditLesson, About} from "./components";
import {mantineCssVariableResolver} from "./themes/varResolver.tsx";
import HomePage from "./components/homePage/HomePage.tsx";
import {UserProvider} from "./providers/UserProvider.tsx";

function App() {

  return (
        <MantineProvider theme={pinkTheme} cssVariablesResolver={mantineCssVariableResolver}>
            <UserProvider>
                <Router>
                    <Routes>
                        <Route path="/" element={<HomePage/>}/>
                        <Route path="/login" element={<Login/>}/>

                        <Route path="/dashboard" element={<LessonsList/>}/>
                        <Route path="/lesson/add" element={<AddLesson/>}/>
                        <Route path="/lesson/:id" element={<LessonView/>}/>
                        <Route path="/lesson/:id/edit" element={<EditLesson/>}/>

                        <Route path="/profile" element={<Profile/>}/>
                        <Route path="/profile/edit" element={<EditProfile/>}/>

                        <Route path="/about" element={<About/>}/>
                        <Route path="*" element={<Navigate to={"/"}/>}/>
                    </Routes>
                </Router>
            </UserProvider>
        </MantineProvider>
  )
}

export default App
