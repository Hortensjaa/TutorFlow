package com.jk.TutorFlow.services;

import com.jk.TutorFlow.entities.File;
import com.jk.TutorFlow.entities.Lesson;
import com.jk.TutorFlow.entities.Student;
import com.jk.TutorFlow.entities.User;
import com.jk.TutorFlow.models.LessonModel;
import com.jk.TutorFlow.repositories.FileRepository;
import com.jk.TutorFlow.repositories.LessonRepository;
import com.jk.TutorFlow.repositories.StudentRepository;
import com.jk.TutorFlow.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    private FileRepository fileRepository;

    public Optional<Lesson> getLesson(Long id) {
        return lessonRepository.findById(id);
    }

    public Set<Lesson> getLessonsByTeacherId(Long teacherId) {
        return lessonRepository.findAllByTeacherId(teacherId);
    }

    public Set<Lesson> getLatestLessons(Long userId) {
        return lessonRepository.findLatest(userId);
    }

    public void deleteLesson(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
        lessonRepository.delete(lesson);
    }

    public Lesson addLesson(LessonModel model, Long teacher_id, Set<File> files) {
        Lesson lesson = new Lesson(model);
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

    public Lesson updateLesson(LessonModel model, Lesson lesson, Set<File> newFiles) {
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
            lesson.addFiles(newFiles);
        }
        lessonRepository.save(lesson);
        return lesson;
    }

    public LessonModel generateModel(Lesson entity) {
        LessonModel model = new LessonModel();
        model.setID(entity.getLesson_id());
        model.setTopic(entity.getTopic());
        model.setDate(entity.getDate().toLocalDate());
        model.setDescription(entity.getDescription());
        model.setRate(entity.getRate());
        model.setStudent(entity.getStudent() != null ? entity.getStudent().getName() : "Deleted student");
        model.setStudentID(entity.getStudent() != null ? entity.getStudent().getStudent_id() : -1);
        model.setTeacher(entity.getTeacher().getUsername());
        model.setFiles(entity.getFiles().stream().map(File::getPath).toArray(String[]::new));
        return model;
    }
}
