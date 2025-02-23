package com.jk.TutorFlow.controllers;

import com.jk.TutorFlow.entities.User;
import com.jk.TutorFlow.models.UserModel;
import com.jk.TutorFlow.services.UserService;
import com.jk.TutorFlow.utils.PrincipalExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Objects;


@RestController
@RequestMapping("/api/user/")
public class UserController {

    @Value("${frontend.url}")
    private String frontendUrl;

    @Autowired
    private UserService userService;

    @Autowired
    private PrincipalExtractor PrincipalExtractor;


    @GetMapping("add_user/")
    public RedirectView addUser(@AuthenticationPrincipal OAuth2User principal) {
        User user = PrincipalExtractor.createUserFromPrincipal(principal);
        UserModel existingUser = userService.getUserByEmail(user.getEmail());

        if (existingUser == null) {
            userService.addUser(user);
            return new RedirectView(frontendUrl + "/profile/edit/");
        } else {
            return new RedirectView(frontendUrl + "/profile/");
        }
    }

    @GetMapping("active/")
    public ResponseEntity<UserModel> getActiveUser(@AuthenticationPrincipal OAuth2User principal) {
        User userData = PrincipalExtractor.getUserFromPrincipal(principal);
        UserModel userModel = userService.getUserByEmail(userData.getEmail());
        return ResponseEntity.ok().body(userModel);
    }

    @PutMapping("update/")
    public ResponseEntity<UserModel> updateUser(@AuthenticationPrincipal OAuth2User principal, @RequestBody UserModel user) {
        User userData = PrincipalExtractor.getUserFromPrincipal(principal);
        if (Objects.equals(userData.getEmail(), user.getEmail())) {
            UserModel updated = userService.updateUser(user);
            return ResponseEntity.ok().body(updated);
        }
        return ResponseEntity.status(403).body(null);
    }
}
