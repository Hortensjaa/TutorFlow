import {IconBellRinging, IconCalendarEvent, IconSchool, IconSettings} from "@tabler/icons-react";

export interface menuItem {
    link: string,
    label: string,
    icon: any
}

export const menuItems: menuItem[] = [
    { link: '', label: 'Notifications', icon: IconBellRinging },
    { link: '/dashboard', label: 'Lessons', icon: IconSchool },
    { link: '', label: 'Schedule', icon: IconCalendarEvent },
    { link: '/profile', label: 'Settings', icon: IconSettings },
];