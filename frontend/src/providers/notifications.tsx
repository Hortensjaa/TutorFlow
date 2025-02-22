import {
    failureNotification, loadingNotification, successNotification, clearNotifications
} from "../components/common/notifications.tsx";

export {clearNotifications};

export const editLoadingNotification = () => {
    loadingNotification("Saving changes...")
}

export const editSuccessNotification = () => {
    successNotification("Changes have been saved")
}

export const editFailureNotification = () => {
    failureNotification("Changes have not been saved")
}

export const logoutLoadingNotification = () => {
    loadingNotification("Logging out...")
}

export const logoutSuccessNotification = () => {
    successNotification("You have been logged out. See you next time!")
}

export const logoutFailureNotification = () => {
    failureNotification("You have not been logged out :/")
}