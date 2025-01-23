package com.jk.TutorFlow.models;

import com.fasterxml.jackson.annotation.*;

public class Lesson {
    private long id;
    private String topic;
    private String description;
    private String student;

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
}
