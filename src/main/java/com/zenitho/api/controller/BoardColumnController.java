package com.zenitho.api.controller;

import com.zenitho.api.entities.BoardColumn;
import com.zenitho.api.service.BoardColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/columns")
public class BoardColumnController {

    @Autowired
    private BoardColumnService columnService;

    @PostMapping
    public BoardColumn createColumn(@RequestParam String title, @RequestParam Long boardId) {
        return columnService.createColumn(title, boardId);
    }

    @GetMapping
    public List<BoardColumn> getAllColumns() {
        return columnService.getAllColumns();
    }

    @GetMapping("/{id}")
    public BoardColumn getColumnById(@PathVariable Long id) {
        return columnService.getColumnById(id);
    }

    @PutMapping("/{id}")
    public BoardColumn updateColumnTitle(@PathVariable Long id, @RequestParam String newTitle) {
        return columnService.updateColumnTitle(id, newTitle);
    }

    @DeleteMapping("/{id}")
    public void deleteColumn(@PathVariable Long id) {
        columnService.deleteColumn(id);
    }

    @PatchMapping("/{id}/position")
    public BoardColumn updateColumnPosition(@PathVariable Long id, @RequestParam double  position) {
        return columnService.updateColumnPosition(id, position);
    }
}
