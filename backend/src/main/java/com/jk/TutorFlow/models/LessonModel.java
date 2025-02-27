package com.jk.TutorFlow.models;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String[] files;
    private Integer rate;
    private TagModel[] tags;

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

    @JsonProperty("files")
    public String[] getFiles() { return files; }
    @JsonProperty("files")
    public void setFiles(String[] value) { this.files = value; }

    @JsonProperty("rate")
    public Integer getRate() { return rate; }
    @JsonProperty("rate")
    public void setRate(Integer value) { this.rate = value; }

    @JsonProperty("tags")
    public TagModel[] getTags() { return tags; }
    @JsonProperty("tags")
    public void setTags(TagModel[] value) { this.tags = value; }
}
