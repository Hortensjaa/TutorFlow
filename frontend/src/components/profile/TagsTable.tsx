import { useEffect, useState } from "react";
import {
    Box,
    Button, Dialog,
    Divider, Group, Table, Text, TextInput
} from "@mantine/core";
import { IconX } from "@tabler/icons-react";
import styles from './Profile.module.css';
import {Tag} from "../../models";
import {addNewTag, deleteTag, getUserTags} from "../../api/tagsApi.ts";
import {useDisclosure} from "@mantine/hooks";

export const TagsTable = () => {
    const [tags, setTags] = useState<Tag[]>([]);
    const [newTag, setNewTag] = useState<string>("");
    const [opened, { toggle, close }] = useDisclosure(false);
    const [tagToDelete, setTagToDelete] = useState<Tag | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        getUserTags()
            .then(setTags)
            .then(() => setLoading(false))
            .catch(console.error);
    }, []);

    const handleDeleteTag = () => {
        deleteTag(tagToDelete?.id.toString())
            .then((id) => setTags(tags.filter((tag) => tag.id.toString() !== id)))
            .catch(console.error);
        close();
    };

    const handleAddTag = () => {
        if (newTag) {
            addNewTag(newTag)
                .then((nt) => setTags([...tags, nt]))
                .catch(console.error);
        }
        setNewTag("");
    };

    return (
        <>
            <Dialog opened={opened} withCloseButton onClose={close} size="lg" radius="md">
                <Text size="sm" mb="xs" fw={500}>
                    Are you sure you want to delete tag {tagToDelete?.name} from all your lessons?
                    This action is irreversible and will happen immediately.
                </Text>
                <Group align="flex-end">
                    <Button onClick={handleDeleteTag}>Delete</Button>
                </Group>
            </Dialog>

            <Divider mt="lg" mb="md" label="My Tags" labelPosition="center" />

            <Box className={styles.boxadd}>
                <TextInput
                    flex={"0.7"}
                    placeholder="New tag"
                    value={newTag}
                    onChange={(event) => setNewTag(event.currentTarget.value)}
                />
                <Button onClick={handleAddTag} flex={"0.26"}>
                    Add tag
                </Button>
            </Box>

            {loading ? (
                <div className={"loading"}>
                    <Text c="dimmed">Loading... </Text>
                </div>
            ) : (
                tags.length > 0 ? (
                    <Table>
                        <Table.Thead>
                            <Table.Tr>
                                <Table.Th className={styles.tableheader}>Tag</Table.Th>
                                <Table.Th className={styles.tableheader}>Delete</Table.Th>
                            </Table.Tr>
                        </Table.Thead>
                        <Table.Tbody>
                            {tags.map((tag) => (
                                <Table.Tr key={tag.id}>
                                    <Table.Td fw={500}>{tag.name}</Table.Td>
                                    <Table.Td onClick={() => {
                                        setTagToDelete(tag)
                                        toggle()
                                    }}>
                                        <IconX />
                                    </Table.Td>
                                </Table.Tr>
                            ))}
                        </Table.Tbody>
                    </Table>
                ) : (
                <Text c="dimmed">No tags available</Text>
            ))}
        </>
    );
};
