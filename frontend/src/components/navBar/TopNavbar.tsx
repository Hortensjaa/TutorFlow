import {useContext, useState} from 'react';
import {Container, Group, Text, UnstyledButton} from '@mantine/core';
import classes from './TopNavbar.module.css';
import {menuItem, menuItems} from "./menuItems.ts";
import {IconLogout} from "@tabler/icons-react";
import {useLocation, useNavigate} from "react-router-dom";
import {UserContext} from "../../providers/UserContext.tsx";


export function TopNavbar() {
    const { state: thisUser, actions } = useContext(UserContext)
    const navigate = useNavigate();
    const [active, setActive] = useState(useLocation().pathname);

    const items = menuItems.map((link: menuItem) => (
        <a
            key={link.label}
            href={link.link}
            className={classes.link}
            data-active={active === link.link || undefined}
            onClick={(event) => {
                event.preventDefault();
                navigate(link.link);
            }}
        >
            <link.icon stroke={1.5} />
        </a>
    ));

    return (
        <header className={classes.header}>
            <Container className={classes.inner}>
                <UnstyledButton onClick={() => navigate('/')}>
                    <Text
                        size="xl"
                        fw={900}
                        variant="gradient"
                        gradient={{from: 'var(--mantine-primary-color-0)', to: 'yellow', deg: 180}}
                    >
                        TF
                    </Text>
                </UnstyledButton>
                <Group gap={0}>
                    {items}
                </Group>
                <a
                    key={"logout"}
                    href={"#"}
                    className={classes.link}
                    style={{color: "var(--mantine-primary-color-1)"}}
                    onClick={(event) => actions.logout}
                >
                    <IconLogout stroke={1.5}/>
                </a>
            </Container>
        </header>
    );
}