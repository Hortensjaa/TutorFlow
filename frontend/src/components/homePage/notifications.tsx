import {notifications} from "@mantine/notifications";

export const loginLoadingNotification = () => {
    notifications.show({
        title: 'Please, be patient...',
        message: 'It might take up to 2 minutes because of hosting issues. If you are not redirected after this time,' +
            ' close and open tab and try again.',
        autoClose: false,
        withCloseButton: false,
        loading: true,
        color: "google-green"
    })
}