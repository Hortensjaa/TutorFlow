package com.jk.TutorFlow.services;

import com.jk.TutorFlow.entities.File;
import com.jk.TutorFlow.entities.Lesson;
import com.jk.TutorFlow.repositories.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FileServiceTest {

    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    private FileService fileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddFiles() {
        // Given
        String[] fileUrls = {"file1.txt", "file2.txt"};

        // When
        Set<File> result = fileService.addFiles(fileUrls);

        // Then
        assertEquals(2, result.size());
        verify(fileRepository, times(2)).save(any(File.class));
    }

    @Test
    void testUpdateFiles() {
        // Given
        Lesson lesson = new Lesson(); // Assuming a Lesson class exists
        Set<File> files = new HashSet<>();
        File file1 = new File("file1.txt");
        File file2 = new File("file2.txt");
        files.add(file1);
        files.add(file2);

        // When
        fileService.updateFiles(lesson, files);

        // Then
        assertEquals(lesson, file1.getLesson());
        assertEquals(lesson, file2.getLesson());
        verify(fileRepository, times(2)).save(any(File.class));
    }
}
