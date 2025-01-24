package com.jk.TutorFlow.repositories;

import com.jk.TutorFlow.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StudentRepository extends JpaRepository<Student, String> { }
