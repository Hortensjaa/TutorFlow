import {IconSchool, IconSettings, IconUser, IconPlus, IconInfoCircle} from "@tabler/icons-react";

export interface menuItem {
    link: string,
    label: string,
    icon: any,
}

export const menuItems: menuItem[] = [
    { link: '/profile', label: 'Profile', icon: IconUser},
    { link: '/lesson/add', label: 'Add lesson', icon: IconPlus},
    { link: '/dashboard', label: 'Lessons', icon: IconSchool},
    { link: '/profile/edit', label: 'Settings', icon: IconSettings},
    { link: '/about', label: 'About', icon: IconInfoCircle},
    // { link: '', label: 'Notifications', icon: IconBellRinging},
    // { link: '', label: 'Schedule', icon: IconCalendarEvent},
];