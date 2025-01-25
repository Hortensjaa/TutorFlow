import {MantineProvider} from "@mantine/core";
import {BrowserRouter as Router, Navigate, Route, Routes} from "react-router-dom";

import {pinkTheme} from "./themes/colorTheme.tsx";
import {LessonsList} from "./components";
import {mantineCssVariableResolver} from "./themes/varResolver.tsx";

function App() {

  return (
        <MantineProvider theme={pinkTheme} cssVariablesResolver={mantineCssVariableResolver} withNormalizeCSS>
            <Router>
                <Routes>
                    <Route path="/dashboard" element={<LessonsList/>}/>
                    <Route path="*" element={<Navigate to={"/dashboard"}/>}/>
                </Routes>
            </Router>
        </MantineProvider>
  )
}

export default App
