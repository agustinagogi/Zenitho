package com.zenitho.api.service;

import com.zenitho.api.entities.BoardColumn;
import com.zenitho.api.entities.Card;
import com.zenitho.api.repositories.BoardColumnRepository;
import com.zenitho.api.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zenitho.api.payload.CardProgress;

import java.util.Collections;
import java.util.List;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private BoardColumnRepository columnRepository;

    public Card createCard (Card newCard, Long columnId) {
        // Buscamos la columna para asegurarnos de que existe
        BoardColumn column = columnRepository.findById(columnId)
                .orElseThrow(() -> new RuntimeException("Column not found with id: " + columnId));

        // Asignamos la columna a la nueva tarjeta
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

    public Card updateCard(Long cardId, Card cardDetails) {
        Card existingCard = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found with id: " + cardId));

        existingCard.setTitle(cardDetails.getTitle());
        existingCard.setContent(cardDetails.getContent()); // 👈 Guardamos el contenido enriquecido

        return cardRepository.save(existingCard);
    }

    @Transactional
    public Card updateCardContent(Long cardId, String newContent) {
        Card existingCard = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found with id: " + cardId));

        existingCard.setContent(newContent);

        return cardRepository.save(existingCard);
    }

    public void deleteCard(Long cardId) {
        if (!cardRepository.existsById(cardId)) {
            throw new RuntimeException("Card not found with id: " + cardId);
        }
        cardRepository.deleteById(cardId);
    }

    // Método para calcular el progreso de las subtareas
    public CardProgress getCardProgress(Long cardId) {
        Card card = getCardById(cardId);
        String jsonContent = card.getContent();

        CardProgress progress = new CardProgress();

        if (jsonContent == null || jsonContent.isEmpty()) {
            progress.setProgressPercentage(0.0);
            progress.setCompletedTasks(0);
            progress.setTotalTasks(0);
            return progress;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonContent);

            int completedTasks = 0;
            int totalTasks = 0;

            JsonNode contentNode = rootNode.path("content");
            if (contentNode.isArray()) {
                for (JsonNode node : contentNode) {
                    if ("taskList".equals(node.path("type").asText())) {
                        JsonNode taskItems = node.path("content");
                        if (taskItems.isArray()) {
                            for (JsonNode taskItem : taskItems) {
                                if ("taskItem".equals(taskItem.path("type").asText())) {
                                    totalTasks++;
                                    if (taskItem.path("attrs").path("checked").asBoolean()) {
                                        completedTasks++;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            progress.setTotalTasks(totalTasks);
            progress.setCompletedTasks(completedTasks);
            if (totalTasks > 0) {
                progress.setProgressPercentage((double) completedTasks / totalTasks * 100);
            } else {
                progress.setProgressPercentage(0.0);
            }

        } catch (Exception e) {
            // Manejar errores si el contenido JSON es inválido
            System.err.println("Error parsing card content JSON: " + e.getMessage());
            progress.setProgressPercentage(0.0);
        }

        return progress;
    }
}
