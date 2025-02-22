import {notifications} from "@mantine/notifications";
import {Anchor} from "@mantine/core";

export const clearNotifications = () => {
    notifications.clean()
}

export const loadingNotification = (message: String) => {
    notifications.show({
        title: 'Wait a moment...',
        message: `${message}...`,
        autoClose: false,
        withCloseButton: false,
        loading: true,
        color: "deep-orange"
    })
}

export const successNotification = (message: String) => {
    notifications.show({
        title: 'Success!',
        message: `${message}`,
        withCloseButton: true,
        color: "google-green",
        autoClose: 3000
    })
}

export const failureNotification = (message: String) => {
    notifications.show({
        title: 'Something didn\'t go right...',
        message: (
            <>
                {message}. Feel free to open a{" "}
                <Anchor span href="https://github.com/Hortensjaa/TutorFlow/issues" target="_blank" rel="noopener noreferrer">
                    Github issue
                </Anchor>{" "}
                if this problem persists.
            </>
        ),
        autoClose: false,
        withCloseButton: true,
        color: "failure-red"
    })
}