import {useMediaQuery} from "@mantine/hooks";
import {Grid, Title} from "@mantine/core";
import {motion} from "framer-motion";

interface elemProps {
    id: number, color: number, text: string, icon: any
}

const Element = (item: elemProps) => {
    const isMobile = useMediaQuery('(max-width: 768px)');

    return (
        <Grid.Col span={4}>
            <motion.div
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                transition={{ delay: 1 + item.id/4, duration: 0.5 }}
                style={{ textAlign: 'center', margin: isMobile? '0 0' : '0 50px' }}
            >
                <item.icon size={64} color={`var(--mantine-primary-color-${item.color})`} />
                <Title order={4} style={{
                    color: `var(--mantine-primary-color-${item.color})`,
                }}>
                    {item.text}
                </Title>
            </motion.div>
        </Grid.Col>
    )
}


export default Element;