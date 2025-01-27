import {MantineProvider} from "@mantine/core";
import {BrowserRouter as Router, Navigate, Route, Routes} from "react-router-dom";

import {pinkTheme} from "./themes/colorTheme.tsx";
import {LessonsList, Login} from "./components";
import {mantineCssVariableResolver} from "./themes/varResolver.tsx";
import HomePage from "./components/homePage/HomePage.tsx";

function App() {

  return (
        <MantineProvider theme={pinkTheme} cssVariablesResolver={mantineCssVariableResolver} withNormalizeCSS>
            <Router>
                <Routes>
                    <Route path="/" element={<HomePage/>}/>
                    <Route path="/dashboard" element={<LessonsList/>}/>
                    <Route path="/login" element={<Login/>}/>
                    <Route path="*" element={<Navigate to={"/"}/>}/>
                </Routes>
            </Router>
        </MantineProvider>
  )
}

export default App
