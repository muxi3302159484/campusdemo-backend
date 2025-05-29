package org.zyy.campusdemobackend.Campus.model;

public class RegisterRequest {
    private String username;
    private String schoolEmail;
    private String studentId;
    private String password;
    private String role;
    private Integer schoolId;         // 新增
    private Integer departmentId;     // 新增

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSchoolEmail() {
        return schoolEmail;
    }

    public void setSchoolEmail(String schoolEmail) {
        this.schoolEmail = schoolEmail;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String toString() {
        return "RegisterRequest{" +
                "username='" + username + '\'' +
                ", schoolEmail='" + schoolEmail + '\'' +
                ", studentId='" + studentId + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", schoolId=" + schoolId +
                ", departmentId=" + departmentId +
                '}';
    }
}