package com.jk.TutorFlow.models;

import com.fasterxml.jackson.annotation.*;
import com.jk.TutorFlow.entities.Student;

public class StudentModel {
    private long id;
    private String name;

    @JsonProperty("id")
    public long getID() { return id; }
    @JsonProperty("id")
    public void setID(long value) { this.id = value; }

    @JsonProperty("name")
    public String getName() { return name; }
    @JsonProperty("name")
    public void setName(String value) { this.name = value; }

    public StudentModel(Student entity) {
        this.id = entity.getStudent_id();
        this.name = entity.getName();
    }
}
