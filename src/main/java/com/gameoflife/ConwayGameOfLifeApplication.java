package com.gameoflife;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for Conway's Game of Life application.
 * This Spring Boot application provides a REST API for simulating Conway's Game of Life.
 */
@SpringBootApplication
public class ConwayGameOfLifeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConwayGameOfLifeApplication.class, args);
    }
}
