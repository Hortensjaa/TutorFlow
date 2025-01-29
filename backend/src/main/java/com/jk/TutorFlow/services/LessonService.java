package com.jk.TutorFlow.services;

import com.jk.TutorFlow.entities.Lesson;
import com.jk.TutorFlow.repositories.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class LessonService {
    @Autowired
    private LessonRepository lessonRepository;

    public Optional<Lesson> getLesson(Long id) {
        return lessonRepository.findById(id);
    }

    public Set<Lesson> getLessonsByTeacherId(Long teacherId) {
        return lessonRepository.findAllByTeacherId(teacherId);
    }

    public Set<Lesson> getLessonsByStudentId(Long studentId) {
        return lessonRepository.findAllByStudentId(studentId);
    }
}
