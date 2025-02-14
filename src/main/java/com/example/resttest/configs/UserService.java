package com.example.resttest.configs;

import com.example.resttest.Models.User;
import com.example.resttest.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser (String name, String password) {
        String hashPassword = passwordEncoder.encode(password);
        User user = new User();
        user.setName(name);
        user.setPassword(hashPassword);

        userRepository.save(user);
    }

}
