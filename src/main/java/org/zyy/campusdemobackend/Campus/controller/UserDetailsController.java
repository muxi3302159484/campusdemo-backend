package org.zyy.campusdemobackend.Campus.controller;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zyy.campusdemobackend.Campus.model.UserDetails;
import org.zyy.campusdemobackend.Campus.model.UserDetailsRequest;
import org.zyy.campusdemobackend.Campus.repository.UserDetailsRepository;
import java.util.Map;
/**
 * 用户详情控制器
 * GitHub: muxi3302159484
 */

@RestController
@RequestMapping("/api/user/details")
public class UserDetailsController {

    @Autowired
    private UserDetailsRepository userDetailsRepository;


    @PostMapping("/update")
    public ResponseEntity<?> updateUserDetails(@RequestBody UserDetailsRequest request) {
        Integer userId = request.getUserId();
        UserDetails userDetails = userDetailsRepository.findById(userId).orElse(null);
        if (userDetails == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "用户详情不存在"));
        }

        if (request.getAvatar() != null) userDetails.setAvatar(request.getAvatar().toString());
        if (request.getRegistrationDate() != null && !request.getRegistrationDate().isEmpty())
            userDetails.setRegistrationDate(LocalDateTime.parse(request.getRegistrationDate()));
        if (request.getLastLogin() != null && !request.getLastLogin().isEmpty())
            userDetails.setLastLogin(LocalDateTime.parse(request.getLastLogin()));
        if (request.getName() != null) userDetails.setName(request.getName());
        if (request.getCollege() != null) userDetails.setCollege(request.getCollege());
        if (request.getMajor() != null) userDetails.setMajor(request.getMajor());

        userDetailsRepository.save(userDetails);
        return ResponseEntity.ok(Map.of("message", "用户信息更新成功"));
    }
}