// 文件：src/main/java/org/zyy/campusdemobackend/Campus/model/UserDetails.java
package org.zyy.campusdemobackend.Campus.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "userdetails")
public class UserDetails {
    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_email_verified")
    private Boolean isEmailVerified;

    @Column(name = "student_id")
    private String studentId;

    @Column(name = "school_id")
    private Integer schoolId;

    @Column(name = "department_id")
    private Integer departmentId;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "college")
    private String college;

    @Column(name = "major")
    private String major;

    @Column(name = "name")
    private String name;

    // --- getter/setter ---
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDateTime registrationDate) { this.registrationDate = registrationDate; }
    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public Boolean getIsEmailVerified() { return isEmailVerified; }
    public void setIsEmailVerified(Boolean isEmailVerified) { this.isEmailVerified = isEmailVerified; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public Integer getSchoolId() { return schoolId; }
    public void setSchoolId(Integer schoolId) { this.schoolId = schoolId; }
    public Integer getDepartmentId() { return departmentId; }
    public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) {
        this.avatar = (avatar == null || avatar.isEmpty()) ? "1" : avatar;
    }
    public String getCollege() { return college; }
    public void setCollege(String college) { this.college = college; }
    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

}