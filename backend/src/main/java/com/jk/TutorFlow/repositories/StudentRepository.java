package com.jk.TutorFlow.repositories;

import com.jk.TutorFlow.entities.Student;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    @NotNull
    Optional<Student> findById(@NotNull Long id);


}
