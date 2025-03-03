package com.gameoflife.service;

import com.gameoflife.exception.BoardNotFoundException;
import com.gameoflife.exception.BoardProcessingException;
import com.gameoflife.model.Board;
import com.gameoflife.model.dto.BoardRequest;
import com.gameoflife.model.dto.BoardResponse;
import com.gameoflife.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of the BoardService interface.
 * Handles board operations and state management.
 */
@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final GameService gameService;

    @Value("${game.max-iterations:1000}")
    private int maxIterations;

    @Autowired
    public BoardServiceImpl(BoardRepository boardRepository, GameService gameService) {
        this.boardRepository = boardRepository;
        this.gameService = gameService;
    }

    /**
     * Creates a new board with the provided initial state.
     */
    @Override
    @Transactional
    public BoardResponse createBoard(BoardRequest boardRequest) {
        if (!boardRequest.isValid()) {
            throw new IllegalArgumentException("Invalid board state provided");
        }

        Board board = new Board();
        board.setGrid(boardRequest.getInitialState());
        board.setWidth(boardRequest.getWidth());
        board.setHeight(boardRequest.getHeight());
        board.setGeneration(0);
        board.setFinalState(false);

        Board savedBoard = boardRepository.save(board);
        return convertToDto(savedBoard);
    }

    /**
     * Retrieves a board by its ID.
     */
    @Override
    @Transactional(readOnly = true)
    public BoardResponse getBoardById(Long id) {
        Board board = findBoardById(id);
        return convertToDto(board);
    }

    /**
     * Computes and returns the next state of a board.
     */
    @Override
    @Transactional
    public BoardResponse getNextState(Long id) {
        Board board = findBoardById(id);
        
        if (board.isFinalState()) {
            return convertToDto(board);
        }

        boolean[][] currentState = board.getGrid();
        boolean[][] nextState = gameService.computeNextGeneration(currentState);
        
        // Check if the board reached a final state
        boolean isFinalState = Arrays.deepEquals(currentState, nextState);
        
        // Create a new board for the next state
        Board nextBoard = new Board();
        nextBoard.setGrid(nextState);
        nextBoard.setWidth(board.getWidth());
        nextBoard.setHeight(board.getHeight());
        nextBoard.setGeneration(board.getGeneration() + 1);
        nextBoard.setFinalState(isFinalState);
        
        Board savedBoard = boardRepository.save(nextBoard);
        return convertToDto(savedBoard);
    }

    /**
     * Computes a state that is a specific number of iterations away.
     */
    @Override
    @Transactional
    public BoardResponse getStateAfterIterations(Long id, int iterations) {
        if (iterations < 0) {
            throw new IllegalArgumentException("Number of iterations must be non-negative");
        }
        
        if (iterations == 0) {
            return getBoardById(id);
        }

        Board board = findBoardById(id);
        boolean[][] currentState = board.getGrid();
        
        // If the board has already reached its final state
        if (board.isFinalState()) {
            return convertToDto(board);
        }

        boolean[][] nextState = currentState;
        boolean isFinalState = false;
        int generation = board.getGeneration();
        
        for (int i = 0; i < iterations && !isFinalState; i++) {
            boolean[][] tempState = gameService.computeNextGeneration(nextState);
            isFinalState = Arrays.deepEquals(nextState, tempState);
            nextState = tempState;
            generation++;
        }
        
        Board resultBoard = new Board();
        resultBoard.setGrid(nextState);
        resultBoard.setWidth(board.getWidth());
        resultBoard.setHeight(board.getHeight());
        resultBoard.setGeneration(generation);
        resultBoard.setFinalState(isFinalState);
        
        Board savedBoard = boardRepository.save(resultBoard);
        return convertToDto(savedBoard);
    }

    /**
     * Computes the final state of a board (if it exists).
     * Uses a hash set to detect cycles in board states.
     */
    @Override
    @Transactional
    public BoardResponse getFinalState(Long id) {
        Board board = findBoardById(id);
        
        if (board.isFinalState()) {
            return convertToDto(board);
        }

        boolean[][] currentState = board.getGrid();
        Set<String> visitedStates = new HashSet<>();
        
        // Hash the initial state and add it to the set
        visitedStates.add(hashState(currentState));
        
        boolean isFinalState = false;
        int generation = board.getGeneration();
        
        for (int i = 0; i < maxIterations && !isFinalState; i++) {
            boolean[][] nextState = gameService.computeNextGeneration(currentState);
            
            // Check if the board has reached a stable state
            if (Arrays.deepEquals(currentState, nextState)) {
                isFinalState = true;
            } else {
                // Check if we've seen this state before (detected a cycle)
                String stateHash = hashState(nextState);
                if (visitedStates.contains(stateHash)) {
                    isFinalState = true;
                } else {
                    visitedStates.add(stateHash);
                }
            }
            
            currentState = nextState;
            generation++;
        }
        
        if (!isFinalState) {
            throw new BoardProcessingException(
                    "Could not determine final state within " + maxIterations + " iterations");
        }
        
        Board resultBoard = new Board();
        resultBoard.setGrid(currentState);
        resultBoard.setWidth(board.getWidth());
        resultBoard.setHeight(board.getHeight());
        resultBoard.setGeneration(generation);
        resultBoard.setFinalState(true);
        
        Board savedBoard = boardRepository.save(resultBoard);
        return convertToDto(savedBoard);
    }

    /**
     * Converts a Board entity to a BoardResponse DTO.
     */
    @Override
    public BoardResponse convertToDto(Board board) {
        boolean[][] state = board.getGrid();
        
        BoardResponse response = BoardResponse.builder()
                .id(board.getId())
                .state(state)
                .width(board.getWidth())
                .height(board.getHeight())
                .generation(board.getGeneration())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .finalState(board.isFinalState())
                .build();
        
        // Calculate the number of live cells
        int liveCellCount = 0;
        for (boolean[] row : state) {
            for (boolean cell : row) {
                if (cell) {
                    liveCellCount++;
                }
            }
        }
        response.setLiveCellCount(liveCellCount);
        
        return response;
    }

    /**
     * Helper method to find a board by ID or throw an exception.
     */
    private Board findBoardById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new BoardNotFoundException(id));
    }

    /**
     * Creates a string hash representation of a board state.
     * Used for cycle detection.
     */
    private String hashState(boolean[][] state) {
        StringBuilder sb = new StringBuilder();
        for (boolean[] row : state) {
            for (boolean cell : row) {
                sb.append(cell ? '1' : '0');
            }
        }
        return sb.toString();
    }
}
