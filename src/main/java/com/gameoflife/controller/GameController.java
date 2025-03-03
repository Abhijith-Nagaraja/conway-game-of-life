package com.gameoflife.controller;

import com.gameoflife.model.dto.BoardRequest;
import com.gameoflife.model.dto.BoardResponse;
import com.gameoflife.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

/**
 * REST controller for Game of Life operations.
 * Provides endpoints to interact with the Game of Life boards.
 */
@RestController
@RequestMapping("/boards")
public class GameController {

    private final BoardService boardService;

    @Autowired
    public GameController(BoardService boardService) {
        this.boardService = boardService;
    }

    /**
     * Creates a new board with the provided initial state.
     * 
     * @param boardRequest The initial board configuration
     * @return The created board with its ID
     */
    @PostMapping
    public ResponseEntity<BoardResponse> createBoard(@Valid @RequestBody BoardRequest boardRequest) {
        BoardResponse createdBoard = boardService.createBoard(boardRequest);
        return new ResponseEntity<>(createdBoard, HttpStatus.CREATED);
    }

    /**
     * Retrieves a board by its ID.
     * 
     * @param id The board ID
     * @return The board state
     */
    @GetMapping("/{id}")
    public ResponseEntity<BoardResponse> getBoardById(@PathVariable Long id) {
        BoardResponse board = boardService.getBoardById(id);
        return ResponseEntity.ok(board);
    }

    /**
     * Computes and returns the next state of a board.
     * 
     * @param id The board ID
     * @return The next board state
     */
    @GetMapping("/{id}/next")
    public ResponseEntity<BoardResponse> getNextState(@PathVariable Long id) {
        BoardResponse nextState = boardService.getNextState(id);
        return ResponseEntity.ok(nextState);
    }

    /**
     * Computes a state that is a specific number of iterations away.
     * 
     * @param id The board ID
     * @param iterations Number of iterations to compute
     * @return The board state after the specified number of iterations
     */
    @GetMapping("/{id}/iterate/{iterations}")
    public ResponseEntity<BoardResponse> getStateAfterIterations(
            @PathVariable Long id,
            @PathVariable @Min(1) int iterations) {
        BoardResponse futureState = boardService.getStateAfterIterations(id, iterations);
        return ResponseEntity.ok(futureState);
    }

    /**
     * Computes the final state of a board (if it exists).
     * 
     * @param id The board ID
     * @return The final board state
     */
    @GetMapping("/{id}/final")
    public ResponseEntity<BoardResponse> getFinalState(@PathVariable Long id) {
        BoardResponse finalState = boardService.getFinalState(id);
        return ResponseEntity.ok(finalState);
    }
}
