package com.jk.TutorFlow.controllers;

import com.jk.TutorFlow.entities.User;
import com.jk.TutorFlow.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.jk.TutorFlow.Consts;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/api/add_user")
    public RedirectView addUser(@AuthenticationPrincipal OAuth2User principal) {
        User user = extractData(principal);
        User existingUser = userService.getUserByEmail(user.getEmail());
        if (existingUser == null) {
            userService.addUser(user);
            return new RedirectView(Consts.frontendUrl + "/profile/edit/" + user.getUser_id());
        }
        return new RedirectView(Consts.frontendUrl + "/profile/" + existingUser.getUser_id());
    }

    private User extractData(@AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");
        String name = principal.getAttribute("name");
        if (name == null) {
            assert email != null;
            name = email.substring(0, email.indexOf('@'));
        }
        return new User(name, email);
    }
}
