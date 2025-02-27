package com.jk.TutorFlow.services;

import com.jk.TutorFlow.entities.Student;
import com.jk.TutorFlow.entities.User;
import com.jk.TutorFlow.mappers.StudentMapper;
import com.jk.TutorFlow.models.StudentModel;
import com.jk.TutorFlow.repositories.LessonRepository;
import com.jk.TutorFlow.repositories.StudentRepository;
import com.jk.TutorFlow.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class StudentService {
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentMapper studentMapper;


    public List<StudentModel> getStudents(Long teacherId) {
        return studentRepository.findStudentsByTeacherId(teacherId)
                .stream().map(e -> studentMapper.toModel(e,
                        lessonRepository.findMostRecentLessonByStudentId(e.getStudent_id())))
                .sorted(Comparator.comparing(StudentModel::getName)).toList();
    }

    public StudentModel addStudent(Long teacherId, String studentName) {
        User teacher = userRepository.findById(teacherId).orElseThrow(() -> new RuntimeException("Teacher not found"));
        Student student = new Student(studentName.substring(0, 1).toUpperCase() + studentName.substring(1));
        student.setTeacher(teacher);
        studentRepository.save(student);
        userRepository.save(teacher);
        return studentMapper.toModel(student, lessonRepository.findMostRecentLessonByStudentId(student.getStudent_id()));
    }

    public void deleteStudent(Long teacherId, Long studentId) {
        User teacher = userRepository.findById(teacherId).orElseThrow(() -> new RuntimeException("Teacher not found"));
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        studentRepository.delete(student);
        teacher.getStudents().remove(student);
        userRepository.save(teacher);
    }
}
