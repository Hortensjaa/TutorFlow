package com.jk.TutorFlow.utils;

import com.jk.TutorFlow.entities.User;
import com.jk.TutorFlow.models.UserModel;
import com.jk.TutorFlow.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class PrincipalExtractor {

    private final UserService userService;

    @Autowired
    public PrincipalExtractor(UserService userService) {
        this.userService = userService;
    }

    public User createUserFromPrincipal(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            throw new AccessDeniedException("User not authenticated");
        }
        String email = principal.getAttribute("email");
        String name = principal.getAttribute("name");
        if (name == null) {
            assert email != null;
            name = email.substring(0, email.indexOf('@'));
        }
        return new User(name, email);
    }

    public User getUserFromPrincipal(@AuthenticationPrincipal OAuth2User principal) {
        String user_email = principal.getAttribute("email");
        if (user_email == null) {
            throw new AccessDeniedException("User not authenticated");
        }
        User user = userService.getEntityByEmail(user_email);
        if (user == null) {
            throw new AccessDeniedException("User not found");
        }
        return user;
    }
}
