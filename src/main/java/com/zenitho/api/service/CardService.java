package com.zenitho.api.service;

import com.zenitho.api.entities.BoardColumn;
import com.zenitho.api.entities.Card;
import com.zenitho.api.repositories.BoardColumnRepository;
import com.zenitho.api.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private BoardColumnRepository columnRepository;

    public Card createCard (String title, Long columnId) {
        // Buscamos la columna para asegurarnos de que existe
        BoardColumn column = columnRepository.findById(columnId)
                .orElseThrow(() -> new RuntimeException("Column not found with id: " + columnId));

        // Creamos la tarjeta

        Card newCard = new Card();
        newCard.setTitle(title);
        newCard.setColumn(column);

        // Guardamos la tarjeta en la BBDD y la devolvemos

        return cardRepository.save(newCard);
    }

    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    public Card getCardById(Long cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found with id: " + cardId));
    }

    public Card updateCardTitle(Long cardId, String newTitle) {
        Card existingCard = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found with id: " + cardId));

        existingCard.setTitle(newTitle);
        // Aquí podríamos añadir lógica para actualizar el contenido, la posición, etc.
        return cardRepository.save(existingCard);
    }

    public void deleteCard(Long cardId) {
        if (!cardRepository.existsById(cardId)) {
            throw new RuntimeException("Card not found with id: " + cardId);
        }
        cardRepository.deleteById(cardId);
    }
}
