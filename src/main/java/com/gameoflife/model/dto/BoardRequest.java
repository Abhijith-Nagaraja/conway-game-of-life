package com.gameoflife.model.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Data Transfer Object for board creation requests.
 * Represents the initial state of a Game of Life board.
 */
public class BoardRequest {

    /**
     * 2D array representing the board state.
     * true = live cell, false = dead cell
     */
    @NotNull(message = "Initial board state is required")
    @Size(min = 1, message = "Board must have at least one row")
    private boolean[][] initialState;

    /**
     * Optional name for this board
     */
    private String name;

    /**
     * Default constructor
     */
    public BoardRequest() {
    }

    /**
     * All-args constructor
     */
    public BoardRequest(boolean[][] initialState, String name) {
        this.initialState = initialState;
        this.name = name;
    }

    /**
     * Validates that the board is rectangular (all rows have the same length)
     * and has at least one column.
     *
     * @return true if the board is valid
     */
    public boolean isValid() {
        if (initialState == null || initialState.length == 0) {
            return false;
        }

        int width = initialState[0].length;
        if (width == 0) {
            return false;
        }

        for (boolean[] row : initialState) {
            if (row.length != width) {
                return false;
            }
        }

        return true;
    }

    /**
     * Gets the width of the board.
     *
     * @return width of the board
     */
    public int getWidth() {
        return initialState[0].length;
    }

    /**
     * Gets the height of the board.
     *
     * @return height of the board
     */
    public int getHeight() {
        return initialState.length;
    }

    // Getters and setters
    public boolean[][] getInitialState() {
        return initialState;
    }

    public void setInitialState(boolean[][] initialState) {
        this.initialState = initialState;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}