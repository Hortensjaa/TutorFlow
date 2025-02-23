package com.jk.TutorFlow.repositories;

import com.jk.TutorFlow.entities.Student;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    @NotNull
    Optional<Student> findById(@NotNull Long id);

    @Query("SELECT u.students FROM User u WHERE u.user_id = :teacherId")
    List<Student> findStudentsByTeacherId(Long teacherId);

}
