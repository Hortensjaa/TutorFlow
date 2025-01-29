package com.jk.TutorFlow.repositories;

import com.jk.TutorFlow.entities.Lesson;
import com.jk.TutorFlow.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
