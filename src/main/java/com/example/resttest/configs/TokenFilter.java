package com.example.resttest.configs;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class TokenFilter extends OncePerRequestFilter { //фильтр выполняется только один раз за запрос
    @Autowired
    private JwtCore jwtCore;
    @Autowired
    private LoginPageService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = null;
        String username = null;
        UserDetails userDetails = null;
        UsernamePasswordAuthenticationToken auth = null;
//        System.out.println("Request: \n" + request + "\nResponse:\n" + response);
        try {
            String headerAuth = request.getHeader("Authorization");
            if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
                jwt = headerAuth.substring(7);
            }
            if (jwt != null) {
                username = jwtCore.getNameFromJwt(jwt);
            }
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                userDetails = userDetailsService.loadUserByUsername(username);
                auth = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                System.out.println(auth);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        filterChain.doFilter(request, response);
        return;
    }
}
