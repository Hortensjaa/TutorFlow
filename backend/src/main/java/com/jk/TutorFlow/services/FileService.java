package com.jk.TutorFlow.services;

import com.jk.TutorFlow.entities.File;
import com.jk.TutorFlow.entities.Lesson;
import com.jk.TutorFlow.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class FileService {
    @Autowired
    private FileRepository fileRepository;

    public Set<File> addFiles(String[] file_urls) {
        Set<File> files = new HashSet<>();
         for (String url: file_urls) {
            File file = new File(url);
            files.add(file);
            fileRepository.save(file);
        }
        return files;
    }

    public void updateFiles(Lesson lesson, Set<File> files) {
        for (File file: files) {
            file.setLesson(lesson);
            fileRepository.save(file);
        }
    }
}
