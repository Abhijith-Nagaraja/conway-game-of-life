package com.gameoflife.service;

import com.gameoflife.exception.BoardNotFoundException;
import com.gameoflife.exception.BoardProcessingException;
import com.gameoflife.model.Board;
import com.gameoflife.model.dto.BoardRequest;
import com.gameoflife.model.dto.BoardResponse;
import com.gameoflife.repository.BoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the BoardService implementation.
 */
class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private GameService gameService;

    @InjectMocks
    private BoardServiceImpl boardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(boardService, "maxIterations", 100);
    }

    @Test
    void testCreateBoard() {
        // Prepare test data
        boolean[][] initialState = {
                {false, true, false},
                {false, true, false},
                {false, true, false}
        };

        BoardRequest request = new BoardRequest(initialState, "Test Board");

        Board savedBoard = new Board();
        savedBoard.setId(1L);
        savedBoard.setGrid(initialState);
        savedBoard.setWidth(3);
        savedBoard.setHeight(3);
        savedBoard.setGeneration(0);
        savedBoard.setFinalState(false);
        savedBoard.setCreatedAt(LocalDateTime.now());
        savedBoard.setUpdatedAt(LocalDateTime.now());

        when(boardRepository.save(any(Board.class))).thenReturn(savedBoard);

        // Call the service method
        BoardResponse response = boardService.createBoard(request);

        // Verify results
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(3, response.getWidth());
        assertEquals(3, response.getHeight());
        assertEquals(0, response.getGeneration());
        assertFalse(response.isFinalState());
        assertEquals(3, response.getLiveCellCount());
    }

    @Test
    void testGetBoardById() {
        // Prepare test data
        boolean[][] state = {
                {false, true, false},
                {false, true, false},
                {false, true, false}
        };

        Board board = new Board();
        board.setId(1L);
        board.setGrid(state);
        board.setWidth(3);
        board.setHeight(3);
        board.setGeneration(0);
        board.setFinalState(false);
        board.setCreatedAt(LocalDateTime.now());
        board.setUpdatedAt(LocalDateTime.now());

        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));

        // Call the service method
        BoardResponse response = boardService.getBoardById(1L);

        // Verify results
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(3, response.getWidth());
        assertEquals(3, response.getHeight());
        assertEquals(0, response.getGeneration());
        assertFalse(response.isFinalState());

        // Verify that the repository was called
        verify(boardRepository, times(1)).findById(1L);
    }

    @Test
    void testGetBoardByIdNotFound() {
        when(boardRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Call the service method and expect exception
        assertThrows(BoardNotFoundException.class, () -> {
            boardService.getBoardById(999L);
        });

        // Verify that the repository was called
        verify(boardRepository, times(1)).findById(999L);
    }

    @Test
    void testGetNextState() {
        // Prepare test data
        boolean[][] currentState = {
                {false, true, false},
                {false, true, false},
                {false, true, false}
        };

        boolean[][] nextState = {
                {false, false, false},
                {true, true, true},
                {false, false, false}
        };

        Board board = new Board();
        board.setId(1L);
        board.setGrid(currentState);
        board.setWidth(3);
        board.setHeight(3);
        board.setGeneration(0);
        board.setFinalState(false);

        Board nextBoard = new Board();
        nextBoard.setId(2L);
        nextBoard.setGrid(nextState);
        nextBoard.setWidth(3);
        nextBoard.setHeight(3);
        nextBoard.setGeneration(1);
        nextBoard.setFinalState(false);

        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));
        when(gameService.computeNextGeneration(any(boolean[][].class))).thenReturn(nextState);
        when(boardRepository.save(any(Board.class))).thenReturn(nextBoard);

        // Call the service method
        BoardResponse response = boardService.getNextState(1L);

        // Verify results
        assertNotNull(response);
        assertEquals(2L, response.getId());
        assertEquals(1, response.getGeneration());
        assertFalse(response.isFinalState());

        // Verify service interactions
        verify(boardRepository, times(1)).findById(1L);
        verify(gameService, times(1)).computeNextGeneration(any(boolean[][].class));
        verify(boardRepository, times(1)).save(any(Board.class));
    }

    @Test
    void testGetNextStateWithFinalState() {
        // Prepare test data - a board that's already in final state
        boolean[][] state = {
                {true, true},
                {true, true}
        };

        Board board = new Board();
        board.setId(1L);
        board.setGrid(state);
        board.setWidth(2);
        board.setHeight(2);
        board.setGeneration(5);
        board.setFinalState(true);

        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));

        // Call the service method
        BoardResponse response = boardService.getNextState(1L);

        // Verify results
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(5, response.getGeneration());
        assertTrue(response.isFinalState());

        // Verify that we didn't attempt to compute the next state
        verify(gameService, never()).computeNextGeneration(any(boolean[][].class));
        verify(boardRepository, never()).save(any(Board.class));
    }

    @Test
    void testGetStateAfterIterations() {
        // Prepare test data
        boolean[][] initialState = {
                {false, true, false},
                {false, true, false},
                {false, true, false}
        };

        Board board = new Board();
        board.setId(1L);
        board.setGrid(initialState);
        board.setWidth(3);
        board.setHeight(3);
        board.setGeneration(0);
        board.setFinalState(false);

        Board resultBoard = new Board();
        resultBoard.setId(3L);
        resultBoard.setWidth(3);
        resultBoard.setHeight(3);
        resultBoard.setGeneration(2);
        resultBoard.setFinalState(true);

        // Create the states for the mock to return
        boolean[][] nextState = {
                {false, false, false},
                {true, true, true},
                {false, false, false}
        };

        boolean[][] finalState = {
                {false, true, false},
                {false, true, false},
                {false, true, false}
        };

        resultBoard.setGrid(finalState);

        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));

        // Use any() matcher instead of specific arrays
        when(gameService.computeNextGeneration(any(boolean[][].class)))
                .thenReturn(nextState)  // First call returns nextState
                .thenReturn(finalState); // Second call returns finalState

        when(boardRepository.save(any(Board.class))).thenReturn(resultBoard);

        // Call the service method
        BoardResponse response = boardService.getStateAfterIterations(1L, 2);

        // Verify results
        assertNotNull(response);
        assertEquals(3L, response.getId());
        assertEquals(2, response.getGeneration());
        assertTrue(response.isFinalState());

        // Verify service interactions
        verify(boardRepository, times(1)).findById(1L);
        verify(gameService, times(2)).computeNextGeneration(any(boolean[][].class));
        verify(boardRepository, times(1)).save(any(Board.class));
    }

    @Test
    void testGetFinalStateWithStablePattern() {
        // Prepare a board that will reach a stable state
        boolean[][] initialState = {
                {false, true, false},
                {false, true, false},
                {false, true, false}
        };

        Board board = new Board();
        board.setId(1L);
        board.setGrid(initialState);
        board.setWidth(3);
        board.setHeight(3);
        board.setGeneration(0);
        board.setFinalState(false);

        boolean[][] oscillator1 = {
                {false, false, false},
                {true, true, true},
                {false, false, false}
        };

        boolean[][] oscillator2 = {
                {false, true, false},
                {false, true, false},
                {false, true, false}
        };

        Board resultBoard = new Board();
        resultBoard.setId(2L);
        resultBoard.setGrid(oscillator2);
        resultBoard.setWidth(3);
        resultBoard.setHeight(3);
        resultBoard.setGeneration(2);
        resultBoard.setFinalState(true);

        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));

        // Use any() matcher with consecutive returns
        when(gameService.computeNextGeneration(any(boolean[][].class)))
                .thenReturn(oscillator1)  // First call returns oscillator1
                .thenReturn(oscillator2); // Second call returns oscillator2

        when(boardRepository.save(any(Board.class))).thenReturn(resultBoard);

        // Call the service method
        BoardResponse response = boardService.getFinalState(1L);

        // Verify results
        assertNotNull(response);
        assertEquals(2L, response.getId());
        assertEquals(2, response.getGeneration());
        assertTrue(response.isFinalState());

        // Verify service interactions
        verify(boardRepository, times(1)).findById(1L);
        verify(gameService, atLeast(2)).computeNextGeneration(any(boolean[][].class));
        verify(boardRepository, times(1)).save(any(Board.class));
    }

    void testGetFinalStateMaxIterationsExceeded() {
        // Prepare test data for a board that never reaches a final state
        boolean[][] state1 = new boolean[3][3];
        state1[0][0] = true;

        Board board = new Board();
        board.setId(1L);
        board.setGrid(state1);
        board.setWidth(3);
        board.setHeight(3);
        board.setGeneration(0);
        board.setFinalState(false);

        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));

        // Import this at the top of your file
        // import java.util.concurrent.atomic.AtomicInteger;

        // Create a more reliable pattern that never reaches a final state
        AtomicInteger counter = new AtomicInteger(0);
        when(gameService.computeNextGeneration(any(boolean[][].class))).thenAnswer(invocation -> {
            boolean[][] output = new boolean[3][3];
            // Cycle through different positions for a live cell
            int count = counter.getAndIncrement() % 9;
            output[count / 3][count % 3] = true;
            return output;
        });

        // Should throw BoardProcessingException after maxIterations
        Exception exception = assertThrows(BoardProcessingException.class, () -> {
            boardService.getFinalState(1L);
        });

        // Verify the exception message
        assertTrue(exception.getMessage().contains("Could not determine final state"));

        // Verify service interactions
        verify(boardRepository, times(1)).findById(1L);
        verify(gameService, times(100)).computeNextGeneration(any(boolean[][].class));
    }

    @Test
    void testConvertToDto() {
        // Prepare test data
        boolean[][] state = {
                {false, true, false},
                {false, true, false},
                {false, true, false}
        };

        Board board = new Board();
        board.setId(1L);
        board.setGrid(state);
        board.setWidth(3);
        board.setHeight(3);
        board.setGeneration(0);
        board.setFinalState(false);
        LocalDateTime now = LocalDateTime.now();
        board.setCreatedAt(now);
        board.setUpdatedAt(now);

        // Call the service method
        BoardResponse response = boardService.convertToDto(board);

        // Verify results
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(3, response.getWidth());
        assertEquals(3, response.getHeight());
        assertEquals(0, response.getGeneration());
        assertFalse(response.isFinalState());
        assertEquals(now, response.getCreatedAt());
        assertEquals(now, response.getUpdatedAt());
        assertEquals(3, response.getLiveCellCount());

        // Check that the state was copied correctly
        boolean[][] responseState = response.getState();
        assertEquals(3, responseState.length);
        assertEquals(3, responseState[0].length);

        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[0].length; j++) {
                assertEquals(state[i][j], responseState[i][j]);
            }
        }
    }
}