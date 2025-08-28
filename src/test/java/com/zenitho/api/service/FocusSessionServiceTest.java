package com.zenitho.api.service;


import com.zenitho.api.entities.Card;
import com.zenitho.api.entities.FocusSession;
import com.zenitho.api.entities.User;
import com.zenitho.api.repositories.CardRepository;
import com.zenitho.api.repositories.FocusSessionRepository;
import com.zenitho.api.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FocusSessionServiceTest {

    @Mock
    private FocusSessionRepository focusSessionRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private FocusSessionService focusSessionService;

    private User user;
    private Card card;
    private FocusSession focusSession;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        card = new Card();
        card.setId(1L);
        focusSession = new FocusSession();
        focusSession.setId(1L);
        focusSession.setStartTime(LocalDateTime.now());
        focusSession.setUser(user);
        focusSession.setCard(card);
    }

    @Test
    void whenStartFocusSession_thenSessionIsSaved() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(focusSessionRepository.save(any(FocusSession.class))).thenReturn(focusSession);

        // Act
        FocusSession result = focusSessionService.startFocusSession(1L, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getCard().getId());
        verify(focusSessionRepository, times(1)).save(any(FocusSession.class));
    }

    @Test
    void whenEndFocusSession_thenEndTimeIsUpdated() {
        // Arrange
        when(focusSessionRepository.findById(1L)).thenReturn(Optional.of(focusSession));
        when(focusSessionRepository.save(any(FocusSession.class))).thenReturn(focusSession);

        // Act
        FocusSession result = focusSessionService.endFocusSession(1L);

        // Assert
        assertNotNull(result.getEndTime());
        verify(focusSessionRepository, times(1)).save(any(FocusSession.class));
    }

    @Test
    void whenGetSessionsForUser_thenSessionsAreReturned() {
        // Arrange
        when(focusSessionRepository.findByUserId(1L)).thenReturn(Collections.singletonList(focusSession));

        // Act
        List<FocusSession> sessions = focusSessionService.getSessionsForUser(1L);

        // Assert
        assertFalse(sessions.isEmpty());
        assertEquals(1, sessions.size());
        verify(focusSessionRepository, times(1)).findByUserId(1L);
    }
}