package org.zyy.campusdemobackend.demos.web.Entity;

public class Admin {
    private Long id;
    private String admin;
    private String password;
    private String email;
    private String role;

    // 无参构造方法
    public Admin() {}

    // 带参构造方法
    public Admin(Long id, String admin, String password, String email, String role) {
        this.id = id;
        this.admin = admin;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    // Getter 和 Setter 方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // toString 方法
    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", admin='" + admin + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
