import {Box, Button, Title, Text, TextInput, Divider} from '@mantine/core';

const Login = () => {
    const backendUrl = import.meta.env.VITE_BACKEND_URL || 'http://localhost:8080';

    const googleLogin = () => {
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
                <TextInput mb="xs" placeholder={"email"}></TextInput>
                <TextInput mb="xs" placeholder={"password"}></TextInput>
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
                    fullWidth
                    size="md"
                    onClick={googleLogin}
                    style={{backgroundColor: "rgb(8,133,27)"}}
                >
                    Continue with Gmail
                </Button>
        </Box>
    );
};

export default Login;
