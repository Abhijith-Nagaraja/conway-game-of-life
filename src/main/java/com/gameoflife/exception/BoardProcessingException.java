package com.gameoflife.exception;

/**
 * Exception thrown when there's an error processing a board state.
 * This could be due to the board not reaching a final state after
 * maximum iterations, or other processing issues.
 */
public class BoardProcessingException extends RuntimeException {
    
    public BoardProcessingException(String message) {
        super(message);
    }
    
    public BoardProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
