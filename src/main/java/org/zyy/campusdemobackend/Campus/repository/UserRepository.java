package org.zyy.campusdemobackend.Campus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zyy.campusdemobackend.Campus.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    boolean existsByStudentIdOrSchoolEmail(String studentId, String schoolEmail);
    List<User> findByUsernameContaining(String keyword); // 新增：支持模糊查找
}