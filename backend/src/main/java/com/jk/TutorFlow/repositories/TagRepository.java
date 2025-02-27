package com.jk.TutorFlow.repositories;

import com.jk.TutorFlow.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query("SELECT tg FROM Tag tg " +
            "WHERE tg.teacher.user_id = :teacherId " +
            "ORDER BY tg.name ASC")
    List<Tag> getTeachersTags(@Param("teacherId") Long teacherId);
}
