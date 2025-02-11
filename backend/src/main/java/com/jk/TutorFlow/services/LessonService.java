package com.jk.TutorFlow.services;

import com.jk.TutorFlow.entities.File;
import com.jk.TutorFlow.entities.Lesson;
import com.jk.TutorFlow.entities.Student;
import com.jk.TutorFlow.entities.User;
import com.jk.TutorFlow.models.LessonModel;
import com.jk.TutorFlow.repositories.LessonRepository;
import com.jk.TutorFlow.repositories.StudentRepository;
import com.jk.TutorFlow.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class LessonService {
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudentRepository studentRepository;

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
        lesson.setFiles(files);
        lesson.setTeacher(teacher);
        lesson.setStudent(student);
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
        model.setStudent(entity.getStudent().getName());
        model.setStudentID(entity.getStudent().getStudent_id());
        model.setTeacher(entity.getTeacher().getUsername());
        model.setFiles(entity.getFiles().stream().map(File::getPath).toArray(String[]::new));
        return model;
    }
}
