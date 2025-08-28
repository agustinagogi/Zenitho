package com.zenitho.api.controller;

import com.zenitho.api.entities.Board;
import com.zenitho.api.jwt.JwtUtils;
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
    private final JwtUtils jwtUtils;

    @Autowired
    public BoardController(BoardService boardService, JwtUtils jwtUtils) {
        this.boardService = boardService;
        this.jwtUtils = jwtUtils;
    }

    // DTO para la solicitud de creación de tablero
    @Data
    static class BoardRequest {
        private String title;
    }

    @PostMapping
    public ResponseEntity<Board> createBoard(@RequestHeader("Authorization") String token, @RequestBody BoardRequest boardRequest) {
        String jwt = token.substring(7);
        Long userId = jwtUtils.getUserIdFromToken(jwt);
        Board newBoard = boardService.createBoard(boardRequest.getTitle(), userId); // 👈 Usa el título del DTO
        return new ResponseEntity<>(newBoard, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Board> getAllBoards () {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // El email es el nombre de autenticación
        Long userId = jwtUtils.getUserIdFromToken(jwtUtils.generateJwtToken(email));
        return boardService.getAllBoardsForUser(userId); // 👈 Usa el nuevo método
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