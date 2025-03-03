package com.gameoflife.exception;

/**
 * Exception thrown when a board with a specific ID cannot be found.
 */
public class BoardNotFoundException extends RuntimeException {
    
    public BoardNotFoundException(Long id) {
        super("Could not find board with id: " + id);
    }
}
