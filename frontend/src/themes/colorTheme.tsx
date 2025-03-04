import {
    Card,
    Container,
    createTheme,
    DEFAULT_THEME,
    MantineColorsTuple,
    Paper,
    rem,
    Select,
    Table
} from "@mantine/core";
import type { MantineThemeOverride } from "@mantine/core";


const palePink: MantineColorsTuple = [
    '#ffeaf3',
    '#fcd4e1',
    '#f4a7bf',
    '#ec779c',
    '#e64f7e',
    '#e3366c',
    '#e22862',
    '#c91a52',
    '#b41148',
    '#9f003e'
];


const deepOrange: MantineColorsTuple = [
    "#fff4e1",
    "#ffe8cc",
    "#fed09b",
    "#fdb766",
    "#fca13a",
    "#fc931d",
    "#fc8c0c",
    "#e17800",
    "#c86a00",
    "#af5a00"
];

const googleGreen: MantineColorsTuple = [
    "#e9fcee",
    "#d9f4e0",
    "#b3e7c1",
    "#8ada9f",
    "#68cf83",
    "#52c871",
    "#45c567",
    "#36ad56",
    "#2b9a4a",
    "#1b853d"
]

const failureRed: MantineColorsTuple = [
    "#ffeaec",
    "#fcd4d7",
    "#f4a7ac",
    "#ec777e",
    "#e64f57",
    "#e3353f",
    "#e22732",
    "#c91a25",
    "#b41220",
    "#9e0419"
]

export const pinkTheme: MantineThemeOverride = createTheme({
    ...DEFAULT_THEME,
    primaryColor: "pale-pink",
    colors: {
        "pale-pink": palePink,
        "deep-orange": deepOrange,
        "google-green": googleGreen,
        "failure-red": failureRed
    },
});