package com.gameoflife.service;

import org.springframework.stereotype.Service;

/**
 * Implementation of GameService that handles the Game of Life rules and logic.
 */
@Service
public class GameServiceImpl implements GameService {

    /**
     * Computes the next generation of cells based on Conway's Game of Life rules:
     * 1. Any live cell with fewer than 2 live neighbors dies (underpopulation)
     * 2. Any live cell with 2 or 3 live neighbors lives to the next generation
     * 3. Any live cell with more than 3 live neighbors dies (overpopulation)
     * 4. Any dead cell with exactly 3 live neighbors becomes alive (reproduction)
     *
     * @param currentState Current state of the board
     * @return The next state after applying Game of Life rules
     */
    @Override
    public boolean[][] computeNextGeneration(boolean[][] currentState) {
        if (currentState == null || currentState.length == 0 || currentState[0].length == 0) {
            return new boolean[0][0];
        }

        int height = currentState.length;
        int width = currentState[0].length;
        boolean[][] nextState = new boolean[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int liveNeighbors = countLiveNeighbors(currentState, row, col);
                boolean isAlive = currentState[row][col];

                // Apply Conway's Game of Life rules
                if (isAlive) {
                    // Rule 1 & 3: Live cell with <2 or >3 neighbors dies
                    // Rule 2: Live cell with 2-3 neighbors survives
                    nextState[row][col] = liveNeighbors == 2 || liveNeighbors == 3;
                } else {
                    // Rule 4: Dead cell with exactly 3 neighbors becomes alive
                    nextState[row][col] = liveNeighbors == 3;
                }
            }
        }

        return nextState;
    }

    /**
     * Counts the number of live neighbors around a cell.
     * Considers all 8 surrounding cells (horizontal, vertical, diagonal).
     *
     * @param grid Current state of the board
     * @param row Row index of the cell
     * @param col Column index of the cell
     * @return Count of live neighbors (0-8)
     */
    @Override
    public int countLiveNeighbors(boolean[][] grid, int row, int col) {
        int count = 0;
        int height = grid.length;
        int width = grid[0].length;
        
        // Check all 8 neighbors
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                // Skip the cell itself
                if (i == 0 && j == 0) {
                    continue;
                }
                
                int newRow = row + i;
                int newCol = col + j;
                
                // Check if the neighbor is within bounds
                if (newRow >= 0 && newRow < height && newCol >= 0 && newCol < width) {
                    if (grid[newRow][newCol]) {
                        count++;
                    }
                }
            }
        }
        
        return count;
    }
}
