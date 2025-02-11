package com.jk.TutorFlow.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jk.TutorFlow.models.Consts;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class ConstsService {
    private final Consts consts;

    private static Consts loadData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // fixme: hardcoded, absolute path
            File file = new File("C:\\Users\\lilka\\IdeaProjects\\TutorFlow\\models\\consts.json");
            return objectMapper.readValue(file, Consts.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load consts.json", e);
        }
    }

    public ConstsService() {
        this.consts = loadData();
    }

    public List<String> getSubjects() {
        return List.of(consts.getSubject());
    }

    public List<String> getPrimarySchoolLevels() {
        return List.of(consts.getPrimarySchool());
    }

    public List<String> getHighSchoolLevels() {
        return List.of(consts.getHighSchool());
    }

    public String getBackendURL() {
        return consts.getBackendURL();
    }

    public String getFrontendURL() {
        return consts.getFrontendURL();
    }
}
