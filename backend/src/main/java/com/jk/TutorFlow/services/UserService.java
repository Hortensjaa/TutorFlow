package com.jk.TutorFlow.services;

import com.jk.TutorFlow.entities.Student;
import com.jk.TutorFlow.entities.User;
import com.jk.TutorFlow.models.UserModel;
import com.jk.TutorFlow.repositories.StudentRepository;
import com.jk.TutorFlow.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudentRepository studentRepository;

    public void addUser(User user) {
        userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
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

    public User updateUser(UserModel model) {
        User user = userRepository.findById(model.getID()).orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(model.getUsername());
        return userRepository.save(user);
    }
}
