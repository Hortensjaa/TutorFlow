package com.jk.TutorFlow.repositories;

import com.jk.TutorFlow.entities.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    @Query("SELECT l FROM Lesson l WHERE l.teacher.user_id = :teacherId " +
            "AND (:studentId IS NULL OR l.student.student_id = :studentId)")
    Page<Lesson> findAllByTeacherId(
            @Param("teacherId") Long teacherId,
            @Param("studentId") Long studentId,
            Pageable pageable);

    @Query("SELECT l FROM Lesson l " +
            "WHERE l.student.student_id = :studentId " +
            "AND l.date < CURRENT_DATE " +
            "ORDER BY l.date DESC " +
            "LIMIT 1")
    Optional<Lesson> findMostRecentLessonByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT l FROM Lesson l " +
            "WHERE l.teacher.user_id = :teacherId " +
            "AND l.date > CURRENT_DATE " +
            "ORDER BY l.date ASC " +
            "LIMIT 5")
    List<Lesson> findUpcomingLessons(@Param("teacherId") Long teacherId);




}
