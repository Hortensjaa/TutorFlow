package com.jk.TutorFlow.services;

import com.jk.TutorFlow.entities.Lesson;
import com.jk.TutorFlow.repositories.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LessonService {
    @Autowired
    private LessonRepository lessonRepository;

    public List<Lesson> getLessons() {
        return lessonRepository.findAll();
    }

    public Optional<Lesson> getLesson(Long id) {
        return lessonRepository.findById(id);
    }
}
