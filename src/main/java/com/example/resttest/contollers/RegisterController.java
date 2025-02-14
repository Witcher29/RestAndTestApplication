package com.example.resttest.contollers;

import com.example.resttest.configs.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {
    private final UserService userService;
    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String getForm () {
        return "registration";
    }

    @PostMapping("/register")
    public String setForm (@RequestParam String username, @RequestParam String password) {
        userService.registerUser(username, password);
        return "redirect:/";
    }
}
