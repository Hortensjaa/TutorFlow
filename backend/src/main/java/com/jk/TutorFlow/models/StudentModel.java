package com.jk.TutorFlow.models;

import com.fasterxml.jackson.annotation.*;
import com.jk.TutorFlow.entities.Student;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
