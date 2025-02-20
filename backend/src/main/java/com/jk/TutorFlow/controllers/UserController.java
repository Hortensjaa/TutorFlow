package com.jk.TutorFlow.controllers;

import com.jk.TutorFlow.entities.Student;
import com.jk.TutorFlow.entities.User;
import com.jk.TutorFlow.models.StudentModel;
import com.jk.TutorFlow.models.UserModel;
import com.jk.TutorFlow.services.StudentService;
import com.jk.TutorFlow.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Map;
import java.util.Objects;


@RestController
public class UserController {

    @Value("${frontend.url}")
    private String frontendUrl;

    @Autowired
    private UserService userService;
    @Autowired
    private StudentService studentService;


    @GetMapping("/api/user/add_user/")
    public RedirectView addUser(@AuthenticationPrincipal OAuth2User principal) {
        User user = extractData(principal);
        User existingUser = userService.getUserByEmail(user.getEmail());

        if (existingUser == null) {
            userService.addUser(user);
            return new RedirectView(frontendUrl + "/profile/edit/");
        } else {
            return new RedirectView(frontendUrl + "/profile/");
        }
    }

    @GetMapping("/api/user/{id}/")
    public User getUserById(@PathVariable String id) {
        return userService.getUserById(Long.valueOf(id));
    }

    @GetMapping("/api/user/active/")
    public ResponseEntity<UserModel> getActiveUser(@AuthenticationPrincipal OAuth2User principal) {
        User userData = extractData(principal);
        User entity = userService.getUserByEmail(userData.getEmail());
        UserModel userModel = new UserModel(entity);
        return ResponseEntity.ok().body(userModel);
    }

    @GetMapping("/api/user/students/")
    public ResponseEntity<List<StudentModel>> getStudents(@AuthenticationPrincipal OAuth2User principal) {
        User userData = extractData(principal);
        User entity = userService.getUserByEmail(userData.getEmail());
        List<StudentModel> students =  userService.getStudents(entity.getUser_id()).stream().map(e -> studentService.generateModel(e)).toList();
        return ResponseEntity.ok().body(students);
    }

    @PutMapping("/api/user/")
    public User updateUser(@AuthenticationPrincipal OAuth2User principal, @RequestBody UserModel user) {
        if (Objects.equals(principal.getAttribute("email"), user.getEmail())) {
            return userService.updateUser(user);
        }
        throw new AccessDeniedException("Email doesn't match");
    }

    @PostMapping("/api/user/add_student/")
    public ResponseEntity<Map<String, Object>> addStudentByTeacher(
            @AuthenticationPrincipal OAuth2User principal,
            @RequestBody Map<String, String> request
    ) {
        User userData = extractData(principal);
        User existingTeacher = userService.getUserByEmail(userData.getEmail());
        String name = request.get("name");
        if (name == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "message", "Name is required"));
        }

        Student student = userService.addStudent(existingTeacher.getUser_id(), name);
        return ResponseEntity.ok(Map.of("success", true, "student", studentService.generateModel(student)));
    }

    @DeleteMapping("/api/user/delete_student/")
    public void deleteStudentByTeacher(
            @AuthenticationPrincipal OAuth2User principal,
            @RequestBody StudentModel studentModel
    ) {
        User userData = extractData(principal);
        User existingTeacher = userService.getUserByEmail(userData.getEmail());
        System.out.println(studentModel.getID());
        System.out.println(studentModel.getName());
        userService.deleteStudent(existingTeacher.getUser_id(), studentModel.getID());
    }

    private User extractData(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            throw new AccessDeniedException("User not found");
        }
        String email = principal.getAttribute("email");
        String name = principal.getAttribute("name");
        if (name == null) {
            assert email != null;
            name = email.substring(0, email.indexOf('@'));
        }
        return new User(name, email);
    }
}
