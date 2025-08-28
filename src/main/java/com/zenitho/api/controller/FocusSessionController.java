package com.zenitho.api.controller;

import com.zenitho.api.entities.FocusSession;
import com.zenitho.api.service.FocusSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/focus")
public class FocusSessionController {
    @Autowired
    private FocusSessionService focusSessionService;

    @PostMapping("/start")
    public FocusSession startFocusSession(@RequestParam Long userId, @RequestParam Long cardId) {
        return focusSessionService.startFocusSession(userId, cardId);
    }

    @PutMapping("/{id}/end")
    public FocusSession endFocusSession(@PathVariable Long id) {
        return focusSessionService.endFocusSession(id);
    }

    @GetMapping("/user/{userId}")
    public List<FocusSession> getSessionsForUser(@PathVariable Long userId) {
        return focusSessionService.getSessionsForUser(userId);
    }
}
