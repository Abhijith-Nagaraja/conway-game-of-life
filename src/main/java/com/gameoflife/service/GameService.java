package com.gameoflife.service;

/**
 * Service interface for Game of Life logic.
 */
public interface GameService {

    /**
     * Computes the next generation of a Game of Life board.
     *
     * @param currentState Current state of the board
     * @return The next state after applying Game of Life rules
     */
    boolean[][] computeNextGeneration(boolean[][] currentState);
    
    /**
     * Counts the number of live neighbors for a cell.
     *
     * @param grid Current state of the board
     * @param row Row index of the cell
     * @param col Column index of the cell
     * @return Count of live neighbors (0-8)
     */
    int countLiveNeighbors(boolean[][] grid, int row, int col);
}
