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
    public Card createCard(@RequestParam String title, @RequestParam Long columnId) {
        return cardService.createCard(title, columnId);
    }

    @GetMapping
    public List<Card> getAllCards() {
        return cardService.getAllCards();
    }

    @GetMapping("/{id}")
    public Card getCardById(@PathVariable Long id) {
        return cardService.getCardById(id);
    }
}
