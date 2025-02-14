package com.example.resttest.contollers;

import com.example.resttest.Models.User;
import com.example.resttest.configs.JsonGet;
import com.example.resttest.configs.JwtCore;
import com.example.resttest.repos.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
public class SecurityController {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtCore jwtCore;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    @Autowired
    public void setJwtCore(JwtCore jwtCore) {
        this.jwtCore = jwtCore;
    }

    @GetMapping("/user")
    public String userAccess (Principal principal) { // или любой класс, реализующий этот интерфейс
        return principal.getName();
    }

    @PostMapping("/getuser")
    public ResponseEntity<Map<String, String>> getUser () {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("Greeting", ", %s".formatted(userDetails.getUsername())));
    }

    @PostMapping("/getuser2")
    public ResponseEntity<Map<String, String>> getUser2 (HttpServletRequest httpServletRequest) {
        UserDetails userDetails = (UserDetails) ((Authentication)(httpServletRequest.getUserPrincipal())).getPrincipal();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("Greeting", ", %s".formatted(userDetails.getUsername())));
    }

    @PostMapping("/getuser3")
    public ResponseEntity<Map<String, String>> getUser3 (@AuthenticationPrincipal UserDetails userDetails) {// для извлечения объекта, реализующего Authentication
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("Greeting", ", %s".formatted(userDetails.getUsername())));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup (@RequestBody JsonGet jsonGet) {
        String username = jsonGet.getUsername();
        String password = jsonGet.getPassword();
        if (userRepository.existsByName(username))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Choose different name");
        User user = new User();
        user.setName(username);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return ResponseEntity.ok("Successssss!!");
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin (@RequestBody JsonGet jsonGet) {
        String username = jsonGet.getUsername();
        String password = jsonGet.getPassword();
        Authentication authentication = null;
        authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtCore.generateToken(authentication);
        return ResponseEntity.ok(jwt);
    }
}
