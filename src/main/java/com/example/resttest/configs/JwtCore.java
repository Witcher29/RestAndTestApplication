package com.example.resttest.configs;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtCore {
    @Value("${secret}")
    private String secret;

    @Value("${lifetime}")
    private Long lifetime;

    public String generateToken(Authentication authentication) {
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder() // JwtBuilder builder = Jwts.builder();
                .setSubject(userDetailsImpl.getUsername()) //Обычно это имя пользователя или идентификатор
                .setIssuedAt(new Date()) //Устанавливает время создания токена
                .setExpiration(new Date((new Date()).getTime() + lifetime)) //Устанавливает срок действия токена
                .signWith(SignatureAlgorithm.HS512, secret.getBytes())//Подписывает токен с использованием указанного алгоритма и ключа.
                .compact(); //Завершает создание токена и возвращает его в виде строки
        //builder.claim("role", "ADMIN"); Добавляет пользовательские данные (claims) в токен. JwtBuilder.claim(String key, Object value):
    }

    public String getNameFromJwt (String token) {
        System.out.println("token from getNameFromJwt: " + token);
        return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody().getSubject();

    }
}
