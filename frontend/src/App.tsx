import { useState } from 'react'
import './App.css'
import {MantineProvider} from "@mantine/core";
import {pinkTheme} from "./themes/colorTheme.tsx";

function App() {
  const [count, setCount] = useState(0)

  return (
        <MantineProvider theme={pinkTheme}>
            {/* Your app here */}
        </MantineProvider>
  )
}

export default App
