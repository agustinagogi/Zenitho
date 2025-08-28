package com.zenitho.api.service;

import com.zenitho.api.entities.Card;
import com.zenitho.api.entities.FocusSession;
import com.zenitho.api.entities.User;
import com.zenitho.api.repositories.CardRepository;
import com.zenitho.api.repositories.FocusSessionRepository;
import com.zenitho.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FocusSessionService {
    @Autowired
    private FocusSessionRepository focusSessionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired // Inyecta la plantilla de mensajería
    private SimpMessagingTemplate messagingTemplate;

    public FocusSession startFocusSession(Long userId, Long cardId){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found with id: " + userId));

        Card card = cardRepository.findById(cardId)
                .orElseThrow(()-> new RuntimeException("Card not found with id: " + cardId));

        FocusSession newSession = new FocusSession();
        newSession.setUser(user);
        newSession.setCard(card);
        newSession.setStartTime(LocalDateTime.now());

        FocusSession savedSession = focusSessionRepository.save(newSession);

        // Envía una actualización a los suscriptores de la tarjeta
        messagingTemplate.convertAndSend("/topic/cards/" + cardId, savedSession);

        return savedSession;
    }

    public FocusSession endFocusSession(Long sessionId){
        FocusSession session = focusSessionRepository.findById(sessionId)
                .orElseThrow(()-> new RuntimeException("Session not found with id: " + sessionId));

        session.setEndTime(LocalDateTime.now());

        FocusSession updatedSession = focusSessionRepository.save(session);

        // Envía una actualización a los suscriptores de la tarjeta
        messagingTemplate.convertAndSend("/topic/cards/" + updatedSession.getCard().getId(), updatedSession);

        return updatedSession;
    }

    public List<FocusSession> getSessionsForUser(Long userId){
        return focusSessionRepository.findByUserId(userId);
    }
}
