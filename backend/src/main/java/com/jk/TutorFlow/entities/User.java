package com.jk.TutorFlow.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jk.TutorFlow.models.UserModel;
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
    private Set<Lesson> taught_lessons = new HashSet<>();

    @OneToMany(mappedBy = "student")
    @JsonManagedReference
    private Set<Lesson> attended_lessons = new HashSet<>();

    @ManyToMany(mappedBy = "users")
    @JsonManagedReference
    Set<Role> roles = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "teacher_students",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    @JsonManagedReference
    private Set<User> students = new HashSet<>();

    @ManyToMany(mappedBy = "students")
    @JsonBackReference
    private Set<User> teachers = new HashSet<>();

    public User(String name, String email, @org.jetbrains.annotations.Nullable String avatar_url) {
        this.username = name;
        this.email = email;
        this.avatar_url = avatar_url;
    }

    public User(UserModel model) {
        this.user_id = model.getID();
        this.avatar_url = model.getAvatar();
        this.email = model.getEmail();
        this.username = model.getUsername();
    }

    public void addRole(Role role) {
        this.roles.add(role);
        role.getUsers().add(this);
    }

    public void deleteRole(Role role) {
        this.roles.remove(role);
        role.getUsers().remove(this);
    }

    public void addStudent(User student) {
        this.students.add(student);
        student.getTeachers().add(this);
    }

    public void deleteStudent(User student) {
        this.students.remove(student);
        student.getTeachers().remove(this);
    }

}
