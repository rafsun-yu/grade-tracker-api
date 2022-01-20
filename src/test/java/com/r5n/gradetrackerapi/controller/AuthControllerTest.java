package com.r5n.gradetrackerapi.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void googleSignInInvalidToken() throws Exception {
        // Test fake token
        mvc.perform(post("/api/auth/google-signin")
                .contentType(MediaType.TEXT_PLAIN)
                .content("<fake token>"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void googleSignInValidToken() throws Exception {
        // Test valid token
        // Make sure the text file contains a valid id token.
        InputStream s = getClass().getClassLoader().getResourceAsStream("valid-id-token-string.txt");
        String validIdToken = new String(s.readAllBytes(), StandardCharsets.UTF_8);

        mvc.perform(post("/api/auth/google-signin")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(validIdToken))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("token"));
    }
}