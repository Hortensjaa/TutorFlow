package com.jk.TutorFlow.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lesson_id;
    private String topic;
    private String description;
    private Date date;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<File> files;

    @ManyToOne
    @JoinColumn(name = "teacher", referencedColumnName = "teacher_id")
    @JsonBackReference
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "student", referencedColumnName = "student_id")
    @JsonBackReference
    private Student student;

}
