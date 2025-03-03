package com.gameoflife.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the GameService implementation.
 */
class GameServiceTest {

    private GameService gameService;

    @BeforeEach
    void setUp() {
        gameService = new GameServiceImpl();
    }

    @Test
    void testEmptyGrid() {
        boolean[][] emptyGrid = new boolean[3][3];
        boolean[][] nextGeneration = gameService.computeNextGeneration(emptyGrid);

        // An empty grid should stay empty
        for (int i = 0; i < nextGeneration.length; i++) {
            for (int j = 0; j < nextGeneration[0].length; j++) {
                assertFalse(nextGeneration[i][j]);
            }
        }
    }

    @Test
    void testUnderpopulation() {
        // A single live cell should die in the next generation
        boolean[][] grid = new boolean[3][3];
        grid[1][1] = true;

        boolean[][] nextGeneration = gameService.computeNextGeneration(grid);
        assertFalse(nextGeneration[1][1]);
    }

    @Test
    void testSurvival() {
        // Create a still life (block pattern)
        boolean[][] block = new boolean[4][4];
        block[1][1] = true;
        block[1][2] = true;
        block[2][1] = true;
        block[2][2] = true;

        boolean[][] nextGeneration = gameService.computeNextGeneration(block);

        // Block should remain unchanged
        assertTrue(nextGeneration[1][1]);
        assertTrue(nextGeneration[1][2]);
        assertTrue(nextGeneration[2][1]);
        assertTrue(nextGeneration[2][2]);
    }

    @Test
    void testReproduction() {
        // Create a pattern where a dead cell will come alive
        boolean[][] grid = new boolean[3][3];
        grid[0][1] = true;
        grid[1][0] = true;
        grid[1][2] = true;

        boolean[][] nextGeneration = gameService.computeNextGeneration(grid);

        // Cell at (1,1) should come alive as it has exactly 3 neighbors
        assertTrue(nextGeneration[1][1]);
    }

    @Test
    void testOverpopulation() {
        // Create a pattern where live cells die due to overpopulation
        boolean[][] grid = new boolean[3][3];
        grid[0][0] = true;
        grid[0][1] = true;
        grid[0][2] = true;
        grid[1][0] = true;
        grid[1][1] = true;

        boolean[][] nextGeneration = gameService.computeNextGeneration(grid);

        // Cell at (1,1) should die as it has more than 3 neighbors
        assertFalse(nextGeneration[1][1]);
    }

    @Test
    void testOscillator() {
        // Create a blinker pattern (period 2 oscillator)
        boolean[][] blinker = new boolean[5][5];
        blinker[2][1] = true;
        blinker[2][2] = true;
        blinker[2][3] = true;

        boolean[][] gen1 = gameService.computeNextGeneration(blinker);

        // Blinker should now be vertical
        assertFalse(gen1[2][1]);
        assertTrue(gen1[1][2]);
        assertTrue(gen1[2][2]);
        assertTrue(gen1[3][2]);
        assertFalse(gen1[2][3]);

        boolean[][] gen2 = gameService.computeNextGeneration(gen1);

        // Blinker should return to original horizontal state
        assertTrue(gen2[2][1]);
        assertTrue(gen2[2][2]);
        assertTrue(gen2[2][3]);
        assertFalse(gen2[1][2]);
        assertFalse(gen2[3][2]);
    }

    @Test
    void testGlider() {
        // Create a glider pattern
        boolean[][] glider = new boolean[6][6];
        glider[1][2] = true;
        glider[2][3] = true;
        glider[3][1] = true;
        glider[3][2] = true;
        glider[3][3] = true;

        boolean[][] nextGeneration = gameService.computeNextGeneration(glider);

        // Check specific cells that should change in a glider pattern
        assertTrue(nextGeneration[2][1]);
        assertTrue(nextGeneration[2][3]);
        assertTrue(nextGeneration[3][2]);
        assertTrue(nextGeneration[3][3]);
        assertTrue(nextGeneration[4][2]);
    }

    @Test
    void testCountLiveNeighbors() {
        boolean[][] grid = new boolean[3][3];
        grid[0][0] = true;
        grid[0][1] = true;
        grid[1][0] = true;

        // Center cell has 3 neighbors
        assertEquals(3, gameService.countLiveNeighbors(grid, 1, 1));

        // Corner cell has 2 neighbors
        assertEquals(2, gameService.countLiveNeighbors(grid, 0, 0));

        // Edge cell has 2 neighbors
        assertEquals(2, gameService.countLiveNeighbors(grid, 0, 1));
    }
}