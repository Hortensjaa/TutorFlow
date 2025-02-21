package com.jk.TutorFlow.services;

import com.jk.TutorFlow.entities.Lesson;
import com.jk.TutorFlow.entities.Student;
import com.jk.TutorFlow.entities.User;
import com.jk.TutorFlow.models.StudentModel;
import com.jk.TutorFlow.repositories.LessonRepository;
import com.jk.TutorFlow.repositories.StudentRepository;
import com.jk.TutorFlow.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class StudentService {
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudentRepository studentRepository;

    public StudentModel generateModel(Student entity) {
        Optional<Lesson> lastLesson = lessonRepository.findMostRecentLessonByStudentId(entity.getStudent_id());

        StudentModel model = new StudentModel();
        model.setID(entity.getStudent_id());
        model.setName(entity.getName());
        if (lastLesson.isPresent()) {
            model.setLastLesson(lastLesson.get().getDate().toLocalDate());
            model.setLastTopic(lastLesson.get().getTopic());
            model.setLastLessonId(lastLesson.get().getLesson_id());
        }
        model.setStudentModelClass("High School 1"); // todo: classes
        return model;
    }

    public Set<Student> getStudents(Long teacherId) {
        return userRepository.findStudentsByTeacherId(teacherId);
    }

    public Student addStudent(Long teacherId, String studentName) {
        User teacher = userRepository.findById(teacherId).orElseThrow(() -> new RuntimeException("Teacher not found"));
        Student student = new Student(studentName);
        student.setTeacher(teacher);
        studentRepository.save(student);
        userRepository.save(teacher);
        return student;
    }

    public void deleteStudent(Long teacherId, Long studentId) {
        User teacher = userRepository.findById(teacherId).orElseThrow(() -> new RuntimeException("Teacher not found"));
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        studentRepository.delete(student);
        teacher.deleteStudent(student);
        userRepository.save(teacher);
    }
}
