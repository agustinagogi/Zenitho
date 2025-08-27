package com.zenitho.api.controller;

import com.zenitho.api.entities.Card;
import com.zenitho.api.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    @Autowired
    CardService cardService;

    @PostMapping
    public Card createCard(@RequestBody Card card, @RequestParam Long columnId) {
        return cardService.createCard(card, columnId);
    }

    @GetMapping
    public List<Card> getAllCards() {
        return cardService.getAllCards();
    }

    @GetMapping("/{id}")
    public Card getCardById(@PathVariable Long id) {
        return cardService.getCardById(id);
    }

    @PutMapping("/{id}")
    public Card updateCard(@PathVariable Long id, @RequestBody Card card) {
        return cardService.updateCard(id, card);
    }

    // Para actualizar solo el contenido de una tarjeta
    @PatchMapping("/{id}/content")
    public Card updateCardContent(@PathVariable Long id, @RequestBody String content) {
        return cardService.updateCardContent(id, content);
    }

    @DeleteMapping("/{id}")
    public void deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
    }
}
