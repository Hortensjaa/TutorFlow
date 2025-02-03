package com.jk.TutorFlow.models;

import com.fasterxml.jackson.annotation.*;
import com.jk.TutorFlow.entities.Lesson;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@NoArgsConstructor
@AllArgsConstructor
public class LessonModel {
    private long id;
    private String topic;
    private LocalDate date;
    private String description;
    private String student;
    private long studentID;
    private String teacher;

    @JsonProperty("id")
    public long getID() { return id; }
    @JsonProperty("id")
    public void setID(long value) { this.id = value; }

    @JsonProperty("topic")
    public String getTopic() { return topic; }
    @JsonProperty("topic")
    public void setTopic(String value) { this.topic = value; }

    @JsonProperty("date")
    public LocalDate getDate() { return date; }
    @JsonProperty("date")
    public void setDate(LocalDate value) { this.date = value; }

    @JsonProperty("description")
    public String getDescription() { return description; }
    @JsonProperty("description")
    public void setDescription(String value) { this.description = value; }

    @JsonProperty("student")
    public String getStudent() { return student; }
    @JsonProperty("student")
    public void setStudent(String value) { this.student = value; }

    @JsonProperty("student_id")
    public long getStudentID() { return studentID; }
    @JsonProperty("student_id")
    public void setStudentID(long value) { this.studentID = value; }

    @JsonProperty("teacher")
    public String getTeacher() { return teacher; }
    @JsonProperty("teacher")
    public void setTeacher(String value) { this.teacher = value; }

    public LessonModel(Lesson entity) {
        this.id = entity.getLesson_id();
        this.topic = entity.getTopic();
        this.description = entity.getDescription();
        this.date = entity.getDate().toLocalDate();
        this.student = entity.getStudent().getName();
        this.teacher = entity.getTeacher().getUsername();
        this.studentID = entity.getStudent().getStudent_id();
    }
}
