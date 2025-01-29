import {IconSchool, IconSettings, IconUser} from "@tabler/icons-react";

export interface menuItem {
    link: string,
    label: string,
    icon: any,
}

export const menuItems: menuItem[] = [
    { link: '/profile', label: 'Profile', icon: IconUser},
    { link: '/dashboard', label: 'Lessons', icon: IconSchool},
    { link: '/profile/edit', label: 'Settings', icon: IconSettings},
    // { link: '', label: 'Notifications', icon: IconBellRinging},
    // { link: '', label: 'Schedule', icon: IconCalendarEvent},
];