package org.zyy.campusdemobackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EntityScan(basePackages = "org.zyy.campusdemobackend.Campus.model")
public class CampusdemoBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(CampusdemoBackendApplication.class, args);
    }

}