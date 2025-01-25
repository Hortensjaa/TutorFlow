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

export const pinkTheme: MantineThemeOverride = createTheme({
    ...DEFAULT_THEME,
    primaryColor: "pale-pink",
    colors: {
        "pale-pink": palePink,
        "deep-orange": deepOrange
    },
});