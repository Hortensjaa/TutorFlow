package com.jk.TutorFlow.models;

import com.fasterxml.jackson.annotation.*;
import com.jk.TutorFlow.Consts;
import com.jk.TutorFlow.entities.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    private long id;
    private String avatar;
    private String username;
    private String email;
    private boolean teacher;
    private boolean student;

    @JsonProperty("id")
    public long getID() { return id; }
    @JsonProperty("id")
    public void setID(long value) { this.id = value; }

    @JsonProperty("avatar")
    public String getAvatar() { return avatar; }
    @JsonProperty("avatar")
    public void setAvatar(String value) { this.avatar = value; }

    @JsonProperty("username")
    public String getUsername() { return username; }
    @JsonProperty("username")
    public void setUsername(String value) { this.username = value; }

    @JsonProperty("email")
    public String getEmail() { return email; }
    @JsonProperty("email")
    public void setEmail(String value) { this.email = value; }

    @JsonProperty("teacher")
    public boolean getTeacher() { return teacher; }
    @JsonProperty("teacher")
    public void setTeacher(boolean value) { this.teacher = value; }

    @JsonProperty("student")
    public boolean getStudent() { return student; }
    @JsonProperty("student")
    public void setStudent(boolean value) { this.student = value; }

    public UserModel(User entity) {
        this.id = entity.getUser_id();
        this.avatar = entity.getAvatar_url();
        this.username = entity.getUsername();
        this.email = entity.getEmail();
        this.teacher = entity.getRoles().stream().anyMatch(role -> role.getName().equals(Consts.teacherRole));
        this.student = entity.getRoles().stream().anyMatch(role -> role.getName().equals(Consts.studentRole));
    }
}
