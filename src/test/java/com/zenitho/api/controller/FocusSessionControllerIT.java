package com.zenitho.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zenitho.api.entities.Card;
import com.zenitho.api.entities.FocusSession;
import com.zenitho.api.entities.User;
import com.zenitho.api.repositories.CardRepository;
import com.zenitho.api.repositories.FocusSessionRepository;
import com.zenitho.api.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FocusSessionControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FocusSessionRepository focusSessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CardRepository cardRepository;

    private User user;
    private Card card;

    @BeforeEach
    void setUp() {
        focusSessionRepository.deleteAll();
        userRepository.deleteAll();
        cardRepository.deleteAll();

        user = userRepository.save(new User("Test User", "testuser", "test@test.com", "password"));
        card = cardRepository.save(new Card());
        card.setTitle("Test Card");
    }

    @Test
    void whenStartFocusSession_thenSessionIsCreated() throws Exception {
        mockMvc.perform(post("/api/focus/start")
                        .param("userId", user.getId().toString())
                        .param("cardId", card.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.startTime").exists());
    }

    @Test
    void whenEndFocusSession_thenSessionIsUpdated() throws Exception {
        // Arrange: Start a session first
        FocusSession session = new FocusSession();
        session.setStartTime(java.time.LocalDateTime.now());
        session.setUser(user);
        session.setCard(card);
        session = focusSessionRepository.save(session);

        // Act & Assert
        mockMvc.perform(put("/api/focus/{id}/end", session.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(session.getId()))
                .andExpect(jsonPath("$.endTime").exists());
    }

    @Test
    void whenGetSessionsForUser_thenSessionsAreReturned() throws Exception {
        // Arrange: Create a session for the user
        FocusSession session = new FocusSession();
        session.setStartTime(java.time.LocalDateTime.now());
        session.setUser(user);
        session.setCard(card);
        focusSessionRepository.save(session);

        // Act & Assert
        mockMvc.perform(get("/api/focus/user/{userId}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(session.getId()))
                .andExpect(jsonPath("$[0].user.id").value(user.getId()));
    }
}