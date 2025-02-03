package com.jk.TutorFlow.models;

import com.fasterxml.jackson.annotation.*;
import com.jk.TutorFlow.entities.User;

public class UserModel {
    private long id;
    private String username;
    private String email;

    @JsonProperty("id")
    public long getID() { return id; }
    @JsonProperty("id")
    public void setID(long value) { this.id = value; }

    @JsonProperty("username")
    public String getUsername() { return username; }
    @JsonProperty("username")
    public void setUsername(String value) { this.username = value; }

    @JsonProperty("email")
    public String getEmail() { return email; }
    @JsonProperty("email")
    public void setEmail(String value) { this.email = value; }

    public UserModel(User entity) {
        this.id = entity.getUser_id();
        this.username = entity.getUsername();
        this.email = entity.getEmail();
    }
}
