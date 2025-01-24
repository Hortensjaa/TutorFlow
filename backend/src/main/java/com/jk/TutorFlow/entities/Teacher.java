package com.jk.TutorFlow.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "teachers")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teacher_id;
    private String username;
    private String email;

    @OneToMany(mappedBy = "teacher")
    @JsonManagedReference
    private List<Lesson> lessons;

}
