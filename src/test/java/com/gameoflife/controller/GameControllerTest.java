package com.gameoflife.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gameoflife.exception.BoardNotFoundException;
import com.gameoflife.exception.BoardProcessingException;
import com.gameoflife.model.dto.BoardRequest;
import com.gameoflife.model.dto.BoardResponse;
import com.gameoflife.service.BoardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for the GameController.
 */
@WebMvcTest(GameController.class)
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BoardService boardService;

    @Test
    void testCreateBoard() throws Exception {
        // Prepare test data
        boolean[][] initialState = {
                {false, true, false},
                {false, true, false},
                {false, true, false}
        };
        BoardRequest request = new BoardRequest(initialState, "Test Board");

        BoardResponse mockResponse = new BoardResponse();
        mockResponse.setId(1L);
        mockResponse.setState(initialState);
        mockResponse.setWidth(3);
        mockResponse.setHeight(3);
        mockResponse.setGeneration(0);
        mockResponse.setCreatedAt(LocalDateTime.now());
        mockResponse.setUpdatedAt(LocalDateTime.now());
        mockResponse.setFinalState(false);
        mockResponse.setLiveCellCount(3);

        when(boardService.createBoard(any(BoardRequest.class))).thenReturn(mockResponse);

        // Perform request and verify
        mockMvc.perform(post("/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.generation").value(0))
                .andExpect(jsonPath("$.liveCellCount").value(3));
    }

    @Test
    void testGetBoardById() throws Exception {
        // Prepare test data
        boolean[][] state = {
                {false, true, false},
                {false, true, false},
                {false, true, false}
        };

        BoardResponse mockResponse = new BoardResponse();
        mockResponse.setId(1L);
        mockResponse.setState(state);
        mockResponse.setWidth(3);
        mockResponse.setHeight(3);
        mockResponse.setGeneration(0);
        mockResponse.setCreatedAt(LocalDateTime.now());
        mockResponse.setUpdatedAt(LocalDateTime.now());
        mockResponse.setFinalState(false);
        mockResponse.setLiveCellCount(3);

        when(boardService.getBoardById(eq(1L))).thenReturn(mockResponse);

        // Perform request and verify
        mockMvc.perform(get("/boards/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.generation").value(0))
                .andExpect(jsonPath("$.liveCellCount").value(3));
    }

    @Test
    void testGetNextState() throws Exception {
        // Prepare test data
        boolean[][] nextState = {
                {false, false, false},
                {true, true, true},
                {false, false, false}
        };

        BoardResponse mockResponse = new BoardResponse();
        mockResponse.setId(2L);
        mockResponse.setState(nextState);
        mockResponse.setWidth(3);
        mockResponse.setHeight(3);
        mockResponse.setGeneration(1);
        mockResponse.setCreatedAt(LocalDateTime.now());
        mockResponse.setUpdatedAt(LocalDateTime.now());
        mockResponse.setFinalState(false);
        mockResponse.setLiveCellCount(3);

        when(boardService.getNextState(eq(1L))).thenReturn(mockResponse);

        // Perform request and verify
        mockMvc.perform(get("/boards/1/next"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.generation").value(1))
                .andExpect(jsonPath("$.liveCellCount").value(3));
    }

    @Test
    void testGetStateAfterIterations() throws Exception {
        // Prepare test data
        boolean[][] futureState = {
                {false, false, false},
                {false, false, false},
                {false, false, false}
        };

        BoardResponse mockResponse = new BoardResponse();
        mockResponse.setId(3L);
        mockResponse.setState(futureState);
        mockResponse.setWidth(3);
        mockResponse.setHeight(3);
        mockResponse.setGeneration(5);
        mockResponse.setCreatedAt(LocalDateTime.now());
        mockResponse.setUpdatedAt(LocalDateTime.now());
        mockResponse.setFinalState(true);
        mockResponse.setLiveCellCount(0);

        when(boardService.getStateAfterIterations(eq(1L), eq(5))).thenReturn(mockResponse);

        // Perform request and verify
        mockMvc.perform(get("/boards/1/iterate/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.generation").value(5))
                .andExpect(jsonPath("$.finalState").value(true))
                .andExpect(jsonPath("$.liveCellCount").value(0));
    }

    @Test
    void testGetFinalState() throws Exception {
        // Prepare test data
        boolean[][] finalState = {
                {false, false, false},
                {false, false, false},
                {false, false, false}
        };

        BoardResponse mockResponse = new BoardResponse();
        mockResponse.setId(4L);
        mockResponse.setState(finalState);
        mockResponse.setWidth(3);
        mockResponse.setHeight(3);
        mockResponse.setGeneration(10);
        mockResponse.setCreatedAt(LocalDateTime.now());
        mockResponse.setUpdatedAt(LocalDateTime.now());
        mockResponse.setFinalState(true);
        mockResponse.setLiveCellCount(0);

        when(boardService.getFinalState(eq(1L))).thenReturn(mockResponse);

        // Perform request and verify
        mockMvc.perform(get("/boards/1/final"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.generation").value(10))
                .andExpect(jsonPath("$.finalState").value(true))
                .andExpect(jsonPath("$.liveCellCount").value(0));
    }

    @Test
    void testGetBoardByIdNotFound() throws Exception {
        when(boardService.getBoardById(anyLong())).thenThrow(new BoardNotFoundException(999L));

        mockMvc.perform(get("/boards/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetFinalStateProcessingException() throws Exception {
        when(boardService.getFinalState(anyLong())).thenThrow(
                new BoardProcessingException("Could not determine final state within maximum iterations"));

        mockMvc.perform(get("/boards/1/final"))
                .andExpect(status().isInternalServerError());
    }
}