package com.jk.TutorFlow.services;

import com.jk.TutorFlow.Consts;
import com.jk.TutorFlow.entities.Role;
import com.jk.TutorFlow.entities.User;
import com.jk.TutorFlow.models.UserModel;
import com.jk.TutorFlow.repositories.RoleRepository;
import com.jk.TutorFlow.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    public void addUser(User user) {
        userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public List<User> getAllStudents() {
        return userRepository.findByRoleName(Consts.studentRole);
    }

    public Set<User> getStudents(Long teacherId) {
        return userRepository.findStudentsByTeacherId(teacherId);
    }

    public void addRoleToUser(Long userId, Long roleId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));
        user.addRole(role);
        userRepository.save(user);
    }

    public void deleteRoleFromUser(Long userId, Long roleId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));
        user.deleteRole(role);
        userRepository.save(user);
    }

    public Boolean isTeacher(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getRoles().stream().anyMatch(role -> role.getName().equals(Consts.teacherRole));
    }

    public Boolean isStudent(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getRoles().stream().anyMatch(role -> role.getName().equals(Consts.studentRole));
    }

    public Boolean addStudent(Long teacherId, Long studentId) {
        User teacher = userRepository.findById(teacherId).orElseThrow(() -> new RuntimeException("Teacher not found"));
        User student = userRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        if (student.getRoles().stream().anyMatch(r -> Objects.equals(r.getName(), Consts.studentRole))) {
            teacher.addStudent(student);
            userRepository.save(teacher);
            return true;
        }
        return false;
    }

    public User deleteStudent(Long teacherId, Long studentId) {
        User teacher = userRepository.findById(teacherId).orElseThrow(() -> new RuntimeException("Teacher not found"));
        User student = userRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        teacher.deleteStudent(student);
        userRepository.save(teacher);
        return teacher;
    }

    public User updateUser(UserModel model) {
        User user = userRepository.findById(model.getID()).orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(model.getUsername());
        user.setAvatar_url(model.getAvatar());
        User res = userRepository.save(user);
        if (model.getStudent()) {
            addRoleToUser(res.getUser_id(), 2L);
        } else {
            deleteRoleFromUser(res.getUser_id(), 2L);
        }
        if (model.getTeacher()) {
            addRoleToUser(res.getUser_id(), 1L);
        } else {
            deleteRoleFromUser(res.getUser_id(), 1L);
        }
        return res;
    }
}
