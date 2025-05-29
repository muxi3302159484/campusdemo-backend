package org.zyy.campusdemobackend.Campus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zyy.campusdemobackend.Campus.model.RegisterRequest;
import org.zyy.campusdemobackend.Campus.model.User;
import org.zyy.campusdemobackend.Campus.model.UserDetails;
import org.zyy.campusdemobackend.Campus.repository.UserRepository;
import org.zyy.campusdemobackend.Campus.repository.UserDetailsRepository;

import java.time.LocalDateTime;

@Service
public class AuthService {


    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 注册用户
    public void registerUser(RegisterRequest request) {
        logger.info("收到注册请求: {}", request);

        if (userRepository.existsByStudentIdOrSchoolEmail(request.getStudentId(), request.getSchoolEmail())) {
            logger.warn("注册失败，用户已存在: 学号={}, 邮箱={}", request.getStudentId(), request.getSchoolEmail());
            throw new IllegalArgumentException("用户已存在");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setSchoolEmail(request.getSchoolEmail());
        user.setStudentId(request.getStudentId());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        userRepository.save(user);

        // 新增：保存用户详细信息
        UserDetails details = new UserDetails();
        details.setUserId(user.getUserId());
        details.setRegistrationDate(LocalDateTime.now());
        details.setSchoolId(request.getSchoolId());
        details.setDepartmentId(request.getDepartmentId());
        details.setIsActive(true);
        details.setIsEmailVerified(false);
        details.setStudentId(request.getStudentId());
        userDetailsRepository.save(details);

        logger.info("用户注册成功: 用户名={}, 学号={}, 邮箱={}", user.getUsername(), user.getStudentId(), user.getSchoolEmail());
    }

    // 检查用户是否存在
    public boolean userExists(String studentId, String schoolEmail) {
        boolean exists = userRepository.existsByStudentIdOrSchoolEmail(studentId, schoolEmail);
        logger.debug("检查用户是否存在: 学号={}, 邮箱={}, 结果={}", studentId, schoolEmail, exists);
        return exists;
    }

    // 验证登录
    public boolean validateUser(String username, String rawPassword) {
        logger.info("收到登录请求: 用户名={}", username);

        User user = userRepository.findByUsername(username);
        if (user == null) {
            logger.warn("登录失败，用户未找到: 用户名={}", username);
            return false;
        }

        boolean isPasswordMatch = passwordEncoder.matches(rawPassword, user.getPasswordHash());
        if (isPasswordMatch) {
            logger.info("登录成功: 用户名={}", username);
            // 更新 lastLogin
            UserDetails details = userDetailsRepository.findByUserId(user.getUserId());
            if (details != null) {
                details.setLastLogin(LocalDateTime.now());
                userDetailsRepository.save(details);
            }
        } else {
            logger.warn("登录失败，密码不匹配: 用户名={}", username);
        }
        return isPasswordMatch;
    }
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

        // 新增：根据 userId 查用户详细信息
        public UserDetails getUserDetailsByUserId(Integer userId)
        {
            return userDetailsRepository.findByUserId(userId);
        }

    }