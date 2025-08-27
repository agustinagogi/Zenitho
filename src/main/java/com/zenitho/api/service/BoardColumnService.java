package com.zenitho.api.service;

import com.zenitho.api.entities.Board;
import com.zenitho.api.entities.BoardColumn;
import com.zenitho.api.repositories.BoardColumnRepository;
import com.zenitho.api.repositories.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardColumnService {

    @Autowired
    private BoardColumnRepository columnRepository;

    @Autowired
    private BoardRepository boardRepository;

    public BoardColumn createColumn(String title, Long boardId){
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found with id: " + boardId));

        BoardColumn newColumn = new BoardColumn();
        newColumn.setBoard(board);
        newColumn.setTitle(title);

        return columnRepository.save(newColumn);
    }

    public List<BoardColumn> getAllColumns(){
        return (List<BoardColumn>) columnRepository.findAll();
    }

    public BoardColumn getColumnById(Long columnId){
        return columnRepository.findById(columnId).orElseThrow(() -> new RuntimeException("Column not found with id: " + columnId));
    }
}
