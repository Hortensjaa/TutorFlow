import {MantineProvider} from "@mantine/core";
import {BrowserRouter as Router, Navigate, Route, Routes} from "react-router-dom";

import {pinkTheme} from "./themes/colorTheme.tsx";
import {LessonsList} from "./components";

function App() {

  return (
        <MantineProvider theme={pinkTheme} withNormalizeCSS>
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
