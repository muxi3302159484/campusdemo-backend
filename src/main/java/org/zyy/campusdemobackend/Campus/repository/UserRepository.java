package org.zyy.campusdemobackend.Campus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zyy.campusdemobackend.Campus.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    boolean existsByStudentIdOrSchoolEmail(String studentId, String schoolEmail);
}