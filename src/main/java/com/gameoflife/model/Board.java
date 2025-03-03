package com.gameoflife.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Represents a Game of Life board state stored in the database.
 * The actual board state is serialized to a byte array for storage.
 */
@Entity
@Table(name = "boards")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int width;
    private int height;

    @Lob
    @Column(name = "state")
    private byte[] stateData;

    private int generation;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private boolean finalState;

    // Default constructor
    public Board() {
    }

    // All-args constructor
    public Board(Long id, int width, int height, byte[] stateData, int generation,
                 LocalDateTime createdAt, LocalDateTime updatedAt, boolean finalState) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.stateData = stateData;
        this.generation = generation;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.finalState = finalState;
    }

    // Add getters and setters for all fields
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    public byte[] getStateData() { return stateData; }
    public void setStateData(byte[] stateData) { this.stateData = stateData; }

    public int getGeneration() { return generation; }
    public void setGeneration(int generation) { this.generation = generation; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public boolean isFinalState() { return finalState; }
    public void setFinalState(boolean finalState) { this.finalState = finalState; }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Keep your getGrid() and setGrid() methods
    @JsonIgnore
    public boolean[][] getGrid() {
        // Existing implementation
        boolean[][] grid = new boolean[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int index = row * width + col;
                int byteIndex = index / 8;
                int bitIndex = index % 8;

                if (byteIndex < stateData.length) {
                    grid[row][col] = ((stateData[byteIndex] >> bitIndex) & 1) == 1;
                }
            }
        }

        return grid;
    }

    public void setGrid(boolean[][] grid) {
        // Existing implementation
        this.height = grid.length;
        this.width = grid[0].length;

        int totalCells = height * width;
        int byteSize = (totalCells + 7) / 8; // Ceiling division

        byte[] data = new byte[byteSize];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (grid[row][col]) {
                    int index = row * width + col;
                    int byteIndex = index / 8;
                    int bitIndex = index % 8;
                    data[byteIndex] |= (1 << bitIndex);
                }
            }
        }

        this.stateData = data;
    }
}
