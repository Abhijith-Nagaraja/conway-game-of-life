package com.gameoflife.model.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for board responses.
 * Contains all the information about a board state to be returned to clients.
 */
public class BoardResponse {

    /**
     * Unique identifier of the board
     */
    private Long id;

    /**
     * Current state of the board
     * true = live cell, false = dead cell
     */
    private boolean[][] state;

    /**
     * Width of the board
     */
    private int width;

    /**
     * Height of the board
     */
    private int height;

    /**
     * Current generation number
     */
    private int generation;

    /**
     * When this board was created
     */
    private LocalDateTime createdAt;

    /**
     * When this board was last updated
     */
    private LocalDateTime updatedAt;

    /**
     * Whether this is a final state (stable or repeating)
     */
    private boolean finalState;

    /**
     * Total number of live cells in the current state
     */
    private int liveCellCount;

    /**
     * Default constructor
     */
    public BoardResponse() {
    }

    /**
     * All-args constructor
     */
    public BoardResponse(Long id, boolean[][] state, int width, int height, int generation,
                         LocalDateTime createdAt, LocalDateTime updatedAt, boolean finalState,
                         int liveCellCount) {
        this.id = id;
        this.state = state;
        this.width = width;
        this.height = height;
        this.generation = generation;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.finalState = finalState;
        this.liveCellCount = liveCellCount;
    }

    /**
     * Builder static method to create a builder instance
     */
    public static BoardResponseBuilder builder() {
        return new BoardResponseBuilder();
    }

    /**
     * Calculates the number of live cells in the board state.
     *
     * @return Count of live cells
     */
    public int calculateLiveCellCount() {
        int count = 0;
        for (boolean[] row : state) {
            for (boolean cell : row) {
                if (cell) {
                    count++;
                }
            }
        }
        return count;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean[][] getState() {
        return state;
    }

    public void setState(boolean[][] state) {
        this.state = state;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isFinalState() {
        return finalState;
    }

    public void setFinalState(boolean finalState) {
        this.finalState = finalState;
    }

    public int getLiveCellCount() {
        return liveCellCount;
    }

    public void setLiveCellCount(int liveCellCount) {
        this.liveCellCount = liveCellCount;
    }

    /**
     * Builder class for BoardResponse
     */
    public static class BoardResponseBuilder {
        private Long id;
        private boolean[][] state;
        private int width;
        private int height;
        private int generation;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private boolean finalState;
        private int liveCellCount;

        BoardResponseBuilder() {
        }

        public BoardResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public BoardResponseBuilder state(boolean[][] state) {
            this.state = state;
            return this;
        }

        public BoardResponseBuilder width(int width) {
            this.width = width;
            return this;
        }

        public BoardResponseBuilder height(int height) {
            this.height = height;
            return this;
        }

        public BoardResponseBuilder generation(int generation) {
            this.generation = generation;
            return this;
        }

        public BoardResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public BoardResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public BoardResponseBuilder finalState(boolean finalState) {
            this.finalState = finalState;
            return this;
        }

        public BoardResponseBuilder liveCellCount(int liveCellCount) {
            this.liveCellCount = liveCellCount;
            return this;
        }

        public BoardResponse build() {
            return new BoardResponse(id, state, width, height, generation, createdAt, updatedAt, finalState, liveCellCount);
        }
    }
}