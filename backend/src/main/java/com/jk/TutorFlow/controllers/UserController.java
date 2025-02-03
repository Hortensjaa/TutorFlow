package com.jk.TutorFlow.controllers;

import com.jk.TutorFlow.entities.User;
import com.jk.TutorFlow.models.UserModel;
import com.jk.TutorFlow.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import com.jk.TutorFlow.Consts;

import java.util.List;
import java.util.Map;
import java.util.Objects;


@RestController
public class UserController {

    public static class AddRoleToUserRequest {
        public String user;
        public String role;
    }

    @Autowired
    private UserService userService;

    @GetMapping("/api/user/add_user")
    public RedirectView addUser(@AuthenticationPrincipal OAuth2User principal) {
        User user = extractData(principal);
        User existingUser = userService.getUserByEmail(user.getEmail());
        if (existingUser == null) {
            userService.addUser(user);
            return new RedirectView(Consts.frontendUrl + "/profile/edit/");
        }
        return new RedirectView(Consts.frontendUrl + "/profile/");
    }

    @GetMapping("/api/user/{id}")
    public User getUserById(@PathVariable String id) {
        return userService.getUserById(Long.valueOf(id));
    }

    @GetMapping("/api/user/active")
    public UserModel getActiveUser(@AuthenticationPrincipal OAuth2User principal) {
        User userData = extractData(principal);
        User entity = userService.getUserByEmail(userData.getEmail());
        return new UserModel(entity);
    }

    @PostMapping("/api/user/add_role")
    public User addRoleToUser(@RequestBody AddRoleToUserRequest request) {
        userService.addRoleToUser(Long.valueOf(request.user), Long.valueOf(request.role));
        return userService.getUserById(Long.valueOf(request.user));
    }

    @PutMapping("/api/user/")
    public User updateUser(@AuthenticationPrincipal OAuth2User principal, @RequestBody UserModel user) {
        if (Objects.equals(principal.getAttribute("email"), user.getEmail())) {
            return userService.updateUser(user);
        }
        throw new AccessDeniedException("Email doesn't match");
    }

    @GetMapping("/api/user/students/all")
    public List<UserModel> getAllStudents() {
        return userService.getAllStudents().stream().map(UserModel::new).toList();
    }

    @GetMapping("/api/user/students")
    public List<UserModel> getStudents(@AuthenticationPrincipal OAuth2User principal) {
        User existingUser = userService.getUserByEmail(principal.getAttribute("email"));
        return userService.getStudents(existingUser.getUser_id()).stream().map(UserModel::new).toList();
    }

    @PostMapping("/api/user/add_student")
    public ResponseEntity<Map<String, Object>> addStudentByTeacher(
            @AuthenticationPrincipal OAuth2User principal,
            @RequestBody Map<String, String> request
    ) {
        User existingTeacher = userService.getUserByEmail(principal.getAttribute("email"));

        if (existingTeacher == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("success", false, "message", "Teacher not found"));
        }

        String email = request.get("email");
        User existingStudent = userService.getUserByEmail(email);
        if (existingStudent == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", "Email not found"));
        }

        Boolean res = userService.addStudent(existingTeacher.getUser_id(), existingStudent.getUser_id());
        if (res) {
            return ResponseEntity.ok(Map.of("success", true, "user", new UserModel(existingStudent)));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "success", false,
                            "message", "Failed to add user - not student."));
        }
    }

    private User extractData(@AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");
        String name = principal.getAttribute("name");
        if (name == null) {
            assert email != null;
            name = email.substring(0, email.indexOf('@'));
        }
        String avatarUrl = principal.getAttribute("picture");
        return new User(name, email, avatarUrl);
    }
}
