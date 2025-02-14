package com.example.resttest.contollers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/")
    public String showMain() {
        return "Ahaha: hohoho";
    }

    @GetMapping("/pup")
    public String showpup() {
        return "AAAAAAAAAAAAAAA";
    }

    @PostMapping("/")
    public String postMain() {
        return "Ahaha: hohoho ";
    }

//    @GetMapping("/secret")
//    public String showSecret() {
//        return "Secret";
//    }

    @GetMapping("/secret")
    public String getUser(@AuthenticationPrincipal OAuth2User user) {
        return "Hello, " + user.getAttribute("name"); // Получить имя пользователя
    }


}
