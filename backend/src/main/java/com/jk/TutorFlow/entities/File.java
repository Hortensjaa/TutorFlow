package com.jk.TutorFlow.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "files")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long file_id;
    private String path;

    @ManyToOne
    @JoinColumn(name = "lesson", referencedColumnName = "lesson_id")
    @JsonBackReference
    private Lesson lesson;

    public File(String path) {
        this.path = path;
    }
}
