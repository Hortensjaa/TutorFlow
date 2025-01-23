package com.jk.TutorFlow.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long student_id;
    private String username;
    private String email;

    @OneToMany(mappedBy = "student")
    @JsonManagedReference
    private List<Lesson> lessons;

}
