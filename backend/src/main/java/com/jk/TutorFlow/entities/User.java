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

    @OneToMany(mappedBy = "teacher")
    @JsonManagedReference
    private Set<Lesson> taught_lessons = new HashSet<>();

    @OneToMany(mappedBy = "teacher")
    @JsonManagedReference
    private Set<Student> students = new HashSet<>();

    public User(String name, String email) {
        this.username = name;
        this.email = email;
    }

    public User(UserModel model) {
        this.user_id = model.getID();
        this.email = model.getEmail();
        this.username = model.getUsername();
    }

    public void addStudent(Student student) {
        this.students.add(student);
        student.setTeacher(this);
    }

    public void deleteStudent(Student student) {
        this.students.remove(student);
    }
}
