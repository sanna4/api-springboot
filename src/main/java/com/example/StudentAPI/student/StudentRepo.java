package com.example.StudentAPI.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepo extends JpaRepository <Student, Long> {

    @Query("SELECT s FROM Student s WHERE s.mail = ?1")
    Optional<Student> findStudentByMail(String mail);
}
