import {failureNotification, loadingNotification, successNotification, clearNotifications} from "../common/notifications.tsx";


export {clearNotifications};

export const addLoadingNotification = () => {
    loadingNotification("Adding lesson...")
}

export const addSuccessNotification = () => {
    successNotification("Your lesson has been added")
}

export const addFailureNotification = () => {
    failureNotification("Your lesson has not been added")
}

export const deleteLoadingNotification = () => {
    loadingNotification("Deleting lesson...")
}

export const deleteSuccessNotification = () => {
    successNotification("Your lesson has been deleted")
}

export const deleteFailureNotification = () => {
    failureNotification("Your lesson has not been deleted")
}

export const editLoadingNotification = () => {
    loadingNotification("Saving changes in lesson...")
}

export const editSuccessNotification = () => {
    successNotification("Changes have been saved")
}

export const editFailureNotification = () => {
    failureNotification("Changes have not been saved")
}
