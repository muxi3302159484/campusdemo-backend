package org.zyy.campusdemobackend.demos.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zyy.campusdemobackend.Campus.model.User;

@Controller
public class BasicController {

    @RequestMapping("/hello")
    @ResponseBody
    public String hello(@RequestParam(name = "name", defaultValue = "unknown user") String name) {
        return "Hello " + name;
    }

    @RequestMapping("/user")
    @ResponseBody
    public User user() {
        User user = new User();
        user.setUsername("theonefx");
        user.setSchoolEmail("theonefx@example.com");
        return user;
    }

    @RequestMapping("/save_user")
    @ResponseBody
    public String saveUser(User u) {
        return "user will save: username=" + u.getUsername() + ", email=" + u.getSchoolEmail();
    }

    @ModelAttribute
    public void parseUser(@RequestParam(name = "username", defaultValue = "unknown user") String username,
                          @RequestParam(name = "email", defaultValue = "unknown@example.com") String email, User user) {
        user.setUsername(username);
        user.setSchoolEmail(email);
    }
}