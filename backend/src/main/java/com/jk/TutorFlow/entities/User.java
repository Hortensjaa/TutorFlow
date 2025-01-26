package com.jk.TutorFlow.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;
    private String username;
    private String email;

    @OneToMany(mappedBy = "teacher")
    @JsonManagedReference
    private Set<Lesson> taught_lessons;

    @OneToMany(mappedBy = "student")
    @JsonManagedReference
    private Set<Lesson> attended_lessons;

    @ManyToMany(mappedBy = "users")
    Set<Role> roles;
}
