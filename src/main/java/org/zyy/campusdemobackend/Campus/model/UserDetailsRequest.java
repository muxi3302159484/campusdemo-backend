package org.zyy.campusdemobackend.Campus.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDetailsRequest {

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("registration_date")
    private String registrationDate;

    @JsonProperty("last_login")
    private String lastLogin;

    private Integer avatar;

    private String name;
    private String college;
    private String major;

    // Getter 和 Setter
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(String registrationDate) { this.registrationDate = registrationDate; }

    public String getLastLogin() { return lastLogin; }
    public void setLastLogin(String lastLogin) { this.lastLogin = lastLogin; }

    public Integer getAvatar() { return avatar; }
    public void setAvatar(Integer avatar) { this.avatar = avatar; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCollege() { return college; }
    public void setCollege(String college) { this.college = college; }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }
}