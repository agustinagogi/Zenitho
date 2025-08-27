package com.zenitho.api.controller;

import com.zenitho.api.entities.BoardColumn;
import com.zenitho.api.service.BoardColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/columns")
public class BoardColumnController {

    @Autowired
    private BoardColumnService columnService;

    @PostMapping
    public BoardColumn createColumn(@RequestParam String title, @RequestParam Long boardId) {
        return columnService.createColumn(title, boardId);
    }
}
