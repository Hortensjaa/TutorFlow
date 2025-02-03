package com.jk.TutorFlow.repositories;

import com.jk.TutorFlow.entities.Student;
import com.jk.TutorFlow.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u.students FROM User u WHERE u.user_id = :teacherId")
    Set<Student> findStudentsByTeacherId(Long teacherId);
}
