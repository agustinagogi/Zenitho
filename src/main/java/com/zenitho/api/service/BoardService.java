package com.zenitho.api.service;

import com.zenitho.api.entities.Board;
import com.zenitho.api.entities.User;
import com.zenitho.api.repositories.BoardRepository;
import com.zenitho.api.repositories.UserRepository;
import org.hibernate.engine.jdbc.mutation.TableInclusionChecker;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class BoardService {

    BoardRepository boardRepository;

    UserRepository userRepository;

    public Board createBoard(String title, Long userId){
        // Buscamos el usuario que crea el tablero
        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Creamos el tablero
        Board newBoard = new Board();

        newBoard.setTitle(title);
        newBoard.setMembers(Collections.singleton(creator));

        return boardRepository.save(newBoard);
    }

    public List<Board> getAllBoards(){
        return boardRepository.findAll();
    }

    public Board getBoardById(Long id){
        return boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));
    }

    public Board updateBoard(Long boardId, String newTitle){
        Board existingBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        existingBoard.setTitle(newTitle);
        return boardRepository.save(existingBoard);
    }

    public void deleteBoard(Long boardId){
        if (!boardRepository.existsById(boardId)) {
            throw new RuntimeException("Board not found");
        }

        boardRepository.deleteById(boardId);
    }
}
