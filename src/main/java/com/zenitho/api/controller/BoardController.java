package com.zenitho.api.controller;

import com.zenitho.api.entities.Board;
import com.zenitho.api.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    @Autowired
    BoardService boardService;

    @PostMapping
    public Board createBoard (@RequestParam String title, @RequestParam Long userId) {
        return boardService.createBoard(title, userId);
    }

    @GetMapping
    public List<Board> getAllBoards () {
        return boardService.getAllBoards();
    }

    @GetMapping("/{id}")
    public Board getBoard (@PathVariable Long id) {
        return boardService.getBoardById(id);
    }

    @PutMapping("/{id}")
    public Board updateBoard (@PathVariable Long id, @RequestParam String newTitle) {
        return boardService.updateBoard(id, newTitle);
    }

    @DeleteMapping("/{id}")
    public void deleteBoard (@PathVariable Long id) {
        boardService.deleteBoard(id);
    }
}
