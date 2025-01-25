package com.jk.TutorFlow.models;

import com.fasterxml.jackson.annotation.*;
import com.jk.TutorFlow.entities.Lesson;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.sql.Date;


@NoArgsConstructor
@AllArgsConstructor
public class LessonModel {
    private long id;
    private String topic;
    private String description;
    private String student;
    private String teacher;
    private Date date;

    @JsonProperty("id")
    public long getID() { return id; }
    @JsonProperty("id")
    public void setID(long value) { this.id = value; }

    @JsonProperty("topic")
    public String getTopic() { return topic; }
    @JsonProperty("topic")
    public void setTopic(String value) { this.topic = value; }

    @JsonProperty("description")
    public String getDescription() { return description; }
    @JsonProperty("description")
    public void setDescription(String value) { this.description = value; }

    @JsonProperty("student")
    public String getStudent() { return student; }
    @JsonProperty("student")
    public void setStudent(String value) { this.student = value; }

    @JsonProperty("teacher")
    public String getTeacher() { return teacher; }
    @JsonProperty("teacher")
    public void setTeacher(String value) { this.teacher = value; }

    @JsonProperty("date")
    public Date getDate() { return date; }
    @JsonProperty("date")
    public void setDate(Date value) { this.date = value; }

    public LessonModel(Lesson entity) {
        this.id = entity.getLesson_id();
        this.topic = entity.getTopic();
        this.description = entity.getDescription();
        this.student = entity.getStudent().getUsername();
        this.teacher = entity.getTeacher().getUsername();
        this.date = entity.getDate();
    }
}
