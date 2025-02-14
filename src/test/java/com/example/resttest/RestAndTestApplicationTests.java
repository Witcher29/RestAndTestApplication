package com.example.resttest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RestAndTestApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void signup() throws Exception {
        // Формируем JSON-объект для запроса
        String jsonRequest = "{ \"username\": \"testuser\", \"password\": \"mypassword\" }";

        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)) // Передаем JSON в теле запроса
                .andExpect(status().isOk())
                .andExpect(content().string("Successssss!!"));
    }

    @Test
    void signin() throws Exception {

        String jsonRequest = "{ \"username\": \"testuser\", \"password\": \"mypassword\" }";

        mockMvc.perform(post("/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)) // Передаем JSON в теле запроса
                .andExpect(status().isOk()) // Ожидаем статус 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Ожидаем JSON в ответе
                .andExpect(jsonPath("$.token").isString());
    }

    @Test
    void getUser3() throws Exception {
        // 1. Аутентификация пользователя и получение JWT-токена
        String jsonRequest = "{ \"username\": \"testuser\", \"password\": \"mypassword\" }";
        String jwt = mockMvc.perform(post("/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // 2. Запрос к /getuser3 с использованием JWT-токена
        mockMvc.perform(get("/getuser3")
                        .header("Authorization", "Bearer " + jwt) // Передаем JWT-токен в заголовке
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Ожидаем статус 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Ожидаем JSON в ответе
                .andExpect(jsonPath("$.Greeting").value(", testuser")); // Ожидаем приветствие с именем пользователя
    }
}