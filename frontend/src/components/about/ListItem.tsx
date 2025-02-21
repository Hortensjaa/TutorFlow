import { Text } from "@mantine/core";
import { useNavigate } from "react-router-dom";

interface ListItemProps {
    text: string;
    linkText?: string;
    linkPath?: string;
}

export function ListItem({ text, linkText, linkPath }: ListItemProps) {
    const navigate = useNavigate();

    return (
        <Text span>
            {text}{" "}
            {linkText && linkPath && (
                <Text
                    span
                    c="var(--mantine-primary-color-7)"
                    inherit
                    onClick={() => linkPath ? navigate(linkPath) : {}}
                    style={{ cursor: "pointer" }}
                >
                    {linkText}
                </Text>
            )}
        </Text>
    );
}
