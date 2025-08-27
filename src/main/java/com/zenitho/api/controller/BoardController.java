package com.zenitho.api.controller;

import com.zenitho.api.entities.Board;
import com.zenitho.api.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    @Autowired
    BoardService boardService;

    @PostMapping
    public Board createBoard (@RequestParam String title, @RequestParam Long userId) {
        return boardService.createBoard(title, userId);
    }
}
