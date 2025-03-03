package com.gameoflife.service;

import com.gameoflife.model.Board;
import com.gameoflife.model.dto.BoardRequest;
import com.gameoflife.model.dto.BoardResponse;

/**
 * Service interface for board-related operations.
 * Handles CRUD operations and board state transformations.
 */
public interface BoardService {

    /**
     * Creates a new board from the initial state.
     *
     * @param boardRequest The initial state and metadata
     * @return The created board response with ID
     */
    BoardResponse createBoard(BoardRequest boardRequest);

    /**
     * Retrieves a board by its ID.
     *
     * @param id The board ID
     * @return The board response
     */
    BoardResponse getBoardById(Long id);

    /**
     * Computes the next state of a board.
     *
     * @param id The board ID
     * @return The updated board state
     */
    BoardResponse getNextState(Long id);

    /**
     * Computes a state that is a specific number of iterations away.
     *
     * @param id The board ID
     * @param iterations Number of iterations to compute
     * @return The board state after the specified number of iterations
     */
    BoardResponse getStateAfterIterations(Long id, int iterations);

    /**
     * Computes the final state of a board (if it exists).
     *
     * @param id The board ID
     * @return The final board state
     */
    BoardResponse getFinalState(Long id);

    /**
     * Converts a Board entity to a BoardResponse DTO.
     *
     * @param board The board entity
     * @return The board response DTO
     */
    BoardResponse convertToDto(Board board);
}
