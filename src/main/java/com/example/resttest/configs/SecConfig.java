package com.example.resttest.configs;

import com.example.resttest.repos.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Configuration
@EnableWebSecurity(/*debug = true*/) //покажет запросы,цепочки фильтров
public class SecConfig {

    private UserService userService;
    @Autowired
    private TokenFilter tokenFilter;

    @Autowired
    UserRepository userRepository;

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        http
                .csrf(x -> x.disable())
                .cors(cors -> cors.disable())
//                .formLogin(form -> form
//                        .loginPage("/login")//с этой строкой нужно сделать эту страницу
//                        .permitAll())
//                .oauth2Login(oauth2 -> oauth2
//                        .loginPage("/login"))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize//можно без -> через and() фигачить
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/registration").permitAll()
                        .requestMatchers("/signin").permitAll()
                        .requestMatchers("/signup").permitAll()
                        .requestMatchers("/user").fullyAuthenticated()
                        .requestMatchers("/pup").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .exceptionHandling(x -> x
                    .authenticationEntryPoint(new AuthenticationEntryPoint() {
                        @Override
                        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                            response.sendRedirect("/"); // при попытке войти неавторизованным на защищённый ресурс или при неправильной авторизации перекинет на эту страницу
                            authException.printStackTrace();
                        }
                    })
                        .accessDeniedHandler(new AccessDeniedHandler() {
                            @Override
                            public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                                accessDeniedException.printStackTrace();
                            }
                        }))
                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService()); // Указываем UserDetailsService
        authProvider.setPasswordEncoder(passwordEncoder()); // Указываем PasswordEncoder
        return authProvider;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new LoginPageService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public AuthenticationManagerBuilder configureAuthenticationManagerBuilder(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//        authenticationManagerBuilder.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
//        return authenticationManagerBuilder;
//    }

    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
