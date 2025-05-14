package org.zyy.campusdemobackend.Campus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zyy.campusdemobackend.Campus.model.LoginRequest;
import org.zyy.campusdemobackend.Campus.model.RegisterRequest;
import org.zyy.campusdemobackend.Campus.service.AuthService;
import org.zyy.campusdemobackend.Campus.service.JwtService;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        boolean isValid = authService.validateUser(request.getUsername(), request.getPassword());
        if (isValid) {
            String token = jwtService.generateToken(request.getUsername());
            Integer userId = authService.getUserIdByUsername(request.getUsername());
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "token", token,
                    "userId", userId,
                    "message", "登录成功"
            ));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "success", false,
                    "message", "用户名或密码错误"
            ));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        System.out.println("收到的注册请求: " + request);

        // 验证数据是否完整
        if (request.getUsername() == null || request.getSchoolEmail() == null ||
                request.getStudentId() == null || request.getPassword() == null || request.getRole() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "注册信息不完整"));
        }

        // 检查用户是否已存在
        if (authService.userExists(request.getStudentId(), request.getSchoolEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "用户已存在"));
        }

        // 保存用户信息
        authService.registerUser(request);

        return ResponseEntity.ok(Map.of("message", "注册成功"));
    }
}