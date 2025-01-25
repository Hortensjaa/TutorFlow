import { CSSVariablesResolver } from "@mantine/core";

export const mantineCssVariableResolver: CSSVariablesResolver = (theme) => ({
    variables: {
        // '--mantine-hero-height': theme.other.heroHeight,
    },
    light: {
        // '--mantine-color-deep-orange': theme.other.deepOrangeLight,
    },
    dark: {
        // '--mantine-color-deep-orange': theme.other.deepOrangeDark,
    },
});
