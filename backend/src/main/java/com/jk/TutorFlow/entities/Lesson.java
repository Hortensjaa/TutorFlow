package com.jk.TutorFlow.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jk.TutorFlow.models.LessonModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Date;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "lessons")
@NoArgsConstructor
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lesson_id;
    private String topic;
    private String description;
    private Integer rate;
    private Date date;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<File> files;

    @ManyToOne
    @JoinColumn(name = "teacher", referencedColumnName = "user_id")
    @JsonBackReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User teacher;

    @ManyToOne
    @JoinColumn(name = "student", referencedColumnName = "student_id")
    @JsonBackReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Student student;


    public Lesson(LessonModel model) {
        this.topic = model.getTopic();
        this.description = model.getDescription();
        this.date = Date.valueOf(model.getDate());
        this.rate = model.getRate();
    }
}
