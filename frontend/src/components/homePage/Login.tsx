import {Box, Button, Title, Text, TextInput, Divider} from '@mantine/core';
import {notifications} from "@mantine/notifications";
import {IconBrandGmail} from "@tabler/icons-react";

const Login = () => {
    const backendUrl = import.meta.env.VITE_BACKEND_URL || 'http://localhost:8080';

    const googleLogin = () => {
        notifications.show({
            title: 'Please be patient',
            message: 'It might take up to 2 minutes because of hosting issues. If you are not redirected after this time,' +
                ' please close and open tab and try again.',
            autoClose: 1200000,
            withCloseButton: true,
            loading: true,
            color: "rgb(8,133,27)"
        })
        window.location.href = `${backendUrl}/oauth2/authorization/google`;
    };

    return (
        <Box
            sx={(theme) => ({
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                height: '100vh',
                backgroundColor:
                    theme.colorScheme === 'dark' ? theme.colors.dark[7] : null,
            })}
        >
                <Title order={2} mb="md">
                    Log in to TutorFlow
                </Title>
                <Text size="sm" c="dimmed" mb="lg">
                    Please log in to access your account.
                </Text>
                <TextInput disabled mb="xs" placeholder={"email"}></TextInput>
                <TextInput disabled mb="xs" placeholder={"password"}></TextInput>
                <Button
                    fullWidth
                    size="md"
                    mb="lg"
                    disabled={true}
                >
                    Log in
                </Button>
                <Divider my="xs" label="or" labelPosition="center" />
                <Button
                    justify="space-between"
                    fullWidth
                    size="md"
                    onClick={googleLogin}
                    style={{backgroundColor: "rgb(8,133,27)"}}
                    leftSection={<IconBrandGmail/>}
                    rightSection={<span/>}
                >
                    Continue with Gmail
                </Button>
        </Box>
    );
};

export default Login;
