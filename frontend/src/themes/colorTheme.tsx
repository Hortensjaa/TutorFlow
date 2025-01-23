import {
    createTheme,
    MantineColorsTuple
} from '@mantine/core';

const myColor: MantineColorsTuple = [
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

export const pinkTheme = createTheme({
    fontFamily: 'Open Sans, sans-serif',
    colors: {
        myColor
    }
});
