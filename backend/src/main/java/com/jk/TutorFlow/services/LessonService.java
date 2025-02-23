package com.jk.TutorFlow.services;

import com.jk.TutorFlow.entities.File;
import com.jk.TutorFlow.entities.Lesson;
import com.jk.TutorFlow.entities.Student;
import com.jk.TutorFlow.entities.User;
import com.jk.TutorFlow.mappers.LessonMapper;
import com.jk.TutorFlow.models.LessonModel;
import com.jk.TutorFlow.repositories.LessonRepository;
import com.jk.TutorFlow.repositories.StudentRepository;
import com.jk.TutorFlow.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.*;

@Service
public class LessonService {
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private LessonMapper lessonMapper;

    public Lesson getLesson(Long id, Long teacher_id) {
        Optional<Lesson> optionalLesson = lessonRepository.findById(id);
        if (optionalLesson.isEmpty()) {
            throw new RuntimeException("Lesson not found with id: " + id);
        }
        Lesson lesson = optionalLesson.get();
        if (!Objects.equals(teacher_id, lesson.getTeacher().getUser_id())) {
            throw new AccessDeniedException("User not authorized to update lesson");
        }
        return lesson;
    }

    public LessonModel getLessonModel(Long id, Long teacher_id) {
        return lessonMapper.toModel(getLesson(id, teacher_id));
    }

    public Page<LessonModel> getLessonsByTeacherId(Long teacherId, Long studentId, int page, int size, String sortBy, boolean descending) {
        Sort sort = descending ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Lesson> lessons = lessonRepository.findAllByTeacherId(teacherId, studentId, pageable);
        return lessons.map(lessonMapper::toModel);
    }

    public void deleteLesson(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
        lessonRepository.delete(lesson);
    }

    public Lesson addLesson(LessonModel model, Long teacher_id, Set<File> files) {
        Lesson lesson = lessonMapper.toEntity(model);
        User teacher = userRepository.findById(teacher_id)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        Student student = studentRepository.findById(model.getStudentID())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        if (files != null && !files.isEmpty()) {
            lesson.setFiles(files);
        }
        lesson.setTeacher(teacher);
        lesson.setStudent(student);
        lessonRepository.save(lesson);
        return lesson;
    }

    private void addFiles(Lesson lesson, Set<File> newFiles) {
        lesson.getFiles().addAll(newFiles);
    }

    public LessonModel updateLesson(LessonModel model, Lesson lesson, Set<File> newFiles) {
        lesson.setTopic(model.getTopic());
        lesson.setDate(Date.valueOf(model.getDate()));
        lesson.setDescription(model.getDescription());
        lesson.setRate(model.getRate());
        lesson.setStudent(studentRepository.findById(model.getStudentID())
                .orElseThrow(() -> new RuntimeException("Student not found")));

        Set<String> modelFilePaths = model.getFiles() != null
                ? new HashSet<>(Arrays.asList(model.getFiles()))
                : Collections.emptySet();

        lesson.getFiles().removeIf(f -> !modelFilePaths.contains(f.getPath()));

        if (newFiles != null && !newFiles.isEmpty()) {
            addFiles(lesson, newFiles);
        }
        lessonRepository.save(lesson);
        return lessonMapper.toModel(lesson);
    }

    public List<LessonModel> upcomingLessons(Long teacher_id) {
        return lessonRepository.findUpcomingLessons(teacher_id).stream().map(lessonMapper::toModel).toList();
    }
}
