import { useState } from 'react';
import {IconLogout} from '@tabler/icons-react';
import {Code, Group, Text, UnstyledButton} from '@mantine/core';
import classes from './SideNavbar.module.css';
import {menuItems, menuItem} from "./menuItems.ts";
import {useNavigate} from "react-router-dom";


function NotificationCircle({ count }: { count: number }) {
    if (count <= 0) return null;
    return (
        <div className={classes.circle}>
            {count}
        </div>
    );
}

export default function SideNavbar() {
    const navigate = useNavigate();
    const [active, setActive] = useState('Lessons');
    const [notificationsCount, setNotificationsCount] = useState(3);

    const links = menuItems.map((item: menuItem) => (
        <a
            className={classes.link}
            data-active={item.label === active || undefined}
            href={item.link}
            key={item.label}
            onClick={(event) => {
                event.preventDefault();
                setActive(item.label);
            }}
        >
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                <item.icon className={classes.linkIcon} stroke={1.5} />
                <span>{item.label}</span>
            </div>
            {item.label === 'Notifications' && (
                <div style={{ position: 'relative', marginLeft: 'auto' }}>
                    <NotificationCircle count={notificationsCount} />
                </div>
            )}
        </a>
    ));


    return (
        <nav className={classes.navbar}>
            <div className={classes.navbarMain}>
            <Group className={classes.header} justify="space-between">
                <UnstyledButton onClick={() => navigate('/')}>
                    <Text
                        size="xl"
                        fw={900}
                        variant="gradient"
                        gradient={{from: 'orange', to: 'var(--mantine-primary-color-5)', deg: 180 }}
                    >
                        Tutor Flow
                    </Text>
                </UnstyledButton>
                <Code fw={700}>v1.0.0</Code>
                </Group>
                {links}
            </div>

            <div className={classes.footer}>
                <a href="#" className={classes.link} onClick={(event) => event.preventDefault()}>
                    <IconLogout className={classes.linkIcon} stroke={1.5} />
                    <span>Logout</span>
                </a>
            </div>
        </nav>
    );
}