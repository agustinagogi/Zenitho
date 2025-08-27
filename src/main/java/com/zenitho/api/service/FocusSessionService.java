package com.zenitho.api.service;

import com.zenitho.api.entities.Card;
import com.zenitho.api.entities.FocusSession;
import com.zenitho.api.entities.User;
import com.zenitho.api.repositories.CardRepository;
import com.zenitho.api.repositories.FocusSessionRepository;
import com.zenitho.api.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

public class FocusSessionService {

    private FocusSessionRepository focusSessionRepository;

    private UserRepository userRepository;

    private CardRepository cardRepository;

    public FocusSession startFocusSession(Long userId, Long cardId){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found with id: " + userId));

        Card card = cardRepository.findById(cardId)
                .orElseThrow(()-> new RuntimeException("Card not found with id: " + cardId));

        FocusSession newSession = new FocusSession();
        newSession.setUser(user);
        newSession.setCard(card);
        newSession.setStartTime(LocalDateTime.now());

        return focusSessionRepository.save(newSession);
    }

    public FocusSession endFocusSession(Long sessionId){
        FocusSession session = focusSessionRepository.findById(sessionId)
                .orElseThrow(()-> new RuntimeException("Session not found with id: " + sessionId));

        session.setEndTime(LocalDateTime.now());

        return focusSessionRepository.save(session);
    }

    public List<FocusSession> getSessionsForUser(Long userId){
        return focusSessionRepository.findByUserId(userId);
    }
}
