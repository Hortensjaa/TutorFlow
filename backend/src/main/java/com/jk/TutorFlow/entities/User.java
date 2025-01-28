package com.jk.TutorFlow.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;
    private String username;
    private String email;

    @Nullable
    private String avatar_url;

    @OneToMany(mappedBy = "teacher")
    @JsonManagedReference
    private Set<Lesson> taught_lessons;

    @OneToMany(mappedBy = "student")
    @JsonManagedReference
    private Set<Lesson> attended_lessons;

    @ManyToMany(mappedBy = "users")
    @JsonManagedReference
    Set<Role> roles;

    public User(String name, String email, @org.jetbrains.annotations.Nullable String avatar_url) {
        this.username = name;
        this.email = email;
        this.avatar_url = avatar_url;
        this.roles = new HashSet<>();
    }

    public void addRole(Role role) {
        if (this.roles == null) {
            this.roles = new HashSet<>();
        }
        this.roles.add(role);
        role.getUsers().add(this);
    }
}
