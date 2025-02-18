package com.jk.TutorFlow.repositories;

import com.jk.TutorFlow.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;


@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    @Query("SELECT l FROM Lesson l " +
            "WHERE l.teacher.user_id = :teacherId " +
            "ORDER BY l.date DESC")
    Set<Lesson> findAllByTeacherId(@Param("teacherId") Long teacherId);

    @Query("SELECT l FROM Lesson l " +
            "WHERE l.teacher.user_id = :userId " +
            "ORDER BY l.date DESC " +
            "LIMIT 3")
    Set<Lesson> findLatest(@Param("userId") Long userId);

    @Query("SELECT l FROM Lesson l " +
            "WHERE l.student.student_id = :studentId " +
            "ORDER BY l.date DESC " +
            "LIMIT 1")
    Optional<Lesson> findMostRecentLessonByStudentId(@Param("studentId") Long studentId);
}
