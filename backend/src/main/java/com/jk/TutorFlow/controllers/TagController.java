package com.jk.TutorFlow.controllers;

import com.jk.TutorFlow.entities.User;
import com.jk.TutorFlow.models.TagModel;
import com.jk.TutorFlow.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/tags/")
public class TagController {
    @Autowired
    private TagService tagService;
    @Autowired
    private com.jk.TutorFlow.utils.PrincipalExtractor PrincipalExtractor;

    @GetMapping("all/")
    public ResponseEntity<List<TagModel>> getAllTags(@AuthenticationPrincipal OAuth2User principal) {
        User user = PrincipalExtractor.getUserFromPrincipal(principal);
        return ResponseEntity.ok().body(tagService.getTeachersTags(user.getUser_id()));
    }

    @PostMapping("add/")
    public ResponseEntity<TagModel> addNewTag(
            @AuthenticationPrincipal OAuth2User principal,
            @RequestBody Map<String, String> request
    ) {
        User user = PrincipalExtractor.getUserFromPrincipal(principal);
        String name = request.get("name");
        TagModel tag = tagService.addNewTag(name, user.getUser_id());
        return ResponseEntity.ok().body(tag);
    }

    @DeleteMapping("delete/")
    public ResponseEntity<Long> deleteTag(
            @AuthenticationPrincipal OAuth2User principal,
            @RequestBody Map<String, String> request
    ) {
        User user = PrincipalExtractor.getUserFromPrincipal(principal);
        Long id = Long.valueOf(request.get("id"));
        tagService.deleteTag(id, user.getUser_id());
        return ResponseEntity.ok().body(id);
    }
}
