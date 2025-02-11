package com.jk.TutorFlow.models;

import com.fasterxml.jackson.annotation.*;
import java.time.LocalDate;

public class StudentModel {
    private long id;
    private String name;
    private LocalDate lastLesson;
    private String lastTopic;
    private String studentModelClass;

    @JsonProperty("id")
    public long getID() { return id; }
    @JsonProperty("id")
    public void setID(long value) { this.id = value; }

    @JsonProperty("name")
    public String getName() { return name; }
    @JsonProperty("name")
    public void setName(String value) { this.name = value; }

    @JsonProperty("last_lesson")
    public LocalDate getLastLesson() { return lastLesson; }
    @JsonProperty("last_lesson")
    public void setLastLesson(LocalDate value) { this.lastLesson = value; }

    @JsonProperty("last_topic")
    public String getLastTopic() { return lastTopic; }
    @JsonProperty("last_topic")
    public void setLastTopic(String value) { this.lastTopic = value; }

    @JsonProperty("class")
    public String getStudentModelClass() { return studentModelClass; }
    @JsonProperty("class")
    public void setStudentModelClass(String value) { this.studentModelClass = value; }
}
