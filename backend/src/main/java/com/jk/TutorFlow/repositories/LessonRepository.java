package com.jk.TutorFlow.repositories;

import com.jk.TutorFlow.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LessonRepository extends JpaRepository<Lesson, String> { }
