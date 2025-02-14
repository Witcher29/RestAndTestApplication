package com.example.resttest.configs;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

public class CustomHttpConfigurer extends AbstractHttpConfigurer<CustomHttpConfigurer, HttpSecurity> {
    @Override
    public void init(HttpSecurity builder) throws Exception { // Вызывается при инициализации конфигурации
        builder.authorizeHttpRequests(x -> x.anyRequest().authenticated()); //конфигураторы можно добавить только в init
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception { //Вызывается при применении конфигурации. Используется для добавления или изменения фильтров и других компонентов
//        builder.authorizeHttpRequests(x -> x.anyRequest().authenticated());
        super.configure(builder);
    }
}
