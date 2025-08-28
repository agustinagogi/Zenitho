package com.zenitho.api.controller;

import com.zenitho.api.entities.Board;
import com.zenitho.api.entities.User;
import com.zenitho.api.jwt.JwtUtils;
import com.zenitho.api.repositories.UserRepository;
import com.zenitho.api.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import lombok.Data; // 👈 Importa Lombok para el DTO

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;
    private final UserRepository userRepository;

    @Autowired
    public BoardController(BoardService boardService, UserRepository userRepository) { // <-- AÑADIDO
        this.boardService = boardService;
        this.userRepository = userRepository; // <-- AÑADIDO
    }

    // DTO para la solicitud de creación de tablero
    @Data
    static class BoardRequest {
        private String title;
    }

    @PostMapping
    public ResponseEntity<Board> createBoard(Authentication authentication, @RequestBody BoardRequest boardRequest) {
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));
        Board newBoard = boardService.createBoard(boardRequest.getTitle(), user.getId());
        return new ResponseEntity<>(newBoard, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Board> getAllBoards(Authentication authentication) {
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));
        return boardService.getAllBoardsForUser(user.getId());
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