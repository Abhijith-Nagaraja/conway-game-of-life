# Conway's Game of Life API

A RESTful API implementation of Conway's Game of Life with persistence capabilities.

![Conway's Game of Life](https://upload.wikimedia.org/wikipedia/commons/e/e5/Gospers_glider_gun.gif)

## Overview

This project provides a complete API for simulating Conway's Game of Life. The implementation allows you to:

1. Create new board states
2. Retrieve board states by ID
3. Compute the next generation of a board
4. Compute a state after a specific number of iterations
5. Find the final state of a board (if one exists)

All board states are persisted to a database, ensuring that the application can recover from crashes or restarts without losing data.

## What is Conway's Game of Life?

Conway's Game of Life is a cellular automaton devised by mathematician John Horton Conway in 1970. It's a zero-player game, meaning its evolution is determined by its initial state, requiring no further input.

The universe of the Game of Life is an infinite, two-dimensional grid of square cells, each in one of two states: alive or dead. Every cell interacts with its eight neighbors according to four simple rules:

1. Any live cell with fewer than two live neighbors dies (underpopulation)
2. Any live cell with two or three live neighbors lives on to the next generation
3. Any live cell with more than three live neighbors dies (overpopulation)
4. Any dead cell with exactly three live neighbors becomes alive (reproduction)

## Technology Stack

- Java 17
- Spring Boot 2.7.5
- Spring Data JPA
- H2 Database
- JUnit 5 for testing
- Maven for build management

## Requirements

- JDK 17 or later
- Maven 3.6+ (or use the Maven wrapper included in the project)

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/yourusername/conway-game-of-life.git
cd conway-game-of-life
```

### Build the Project

```bash
./mvnw clean install -Dmaven.test.skip=true
```

### Run the Application

```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080/api`.

### Run Tests

```bash
./mvnw test
```

To generate a test coverage report:

```bash
./mvnw clean verify
```

The coverage report will be available at `target/site/jacoco/index.html`.

## Project Structure

```
conway-game-of-life/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── gameoflife/
│   │   │           ├── ConwayGameOfLifeApplication.java
│   │   │           ├── controller/
│   │   │           │   └── GameController.java
│   │   │           ├── model/
│   │   │           │   ├── Board.java
│   │   │           │   └── dto/
│   │   │           │       ├── BoardRequest.java
│   │   │           │       └── BoardResponse.java
│   │   │           ├── repository/
│   │   │           │   └── BoardRepository.java
│   │   │           ├── service/
│   │   │           │   ├── BoardService.java
│   │   │           │   ├── BoardServiceImpl.java
│   │   │           │   ├── GameService.java
│   │   │           │   └── GameServiceImpl.java
│   │   │           └── exception/
│   │   │               ├── BoardNotFoundException.java
│   │   │               ├── BoardProcessingException.java
│   │   │               └── GlobalExceptionHandler.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── com/
│               └── gameoflife/
│                   ├── controller/
│                   │   └── GameControllerTest.java
│                   ├── service/
│                   │   ├── BoardServiceTest.java
│                   │   └── GameServiceTest.java
│                   └── ConwayGameOfLifeApplicationTests.java
└── pom.xml
```

## Database Configuration

By default, the application uses an H2 database with file-based persistence. The database file is stored in the `./data` directory. You can customize the database configuration in the `application.properties` file.

## API Documentation

For a complete description of all API endpoints, request/response formats, and examples, please refer to the [API Documentation](API_DOCUMENTATION.md).

## Using the API

### Creating a New Board

```bash
curl -X POST http://localhost:8080/api/boards \
  -H "Content-Type: application/json" \
  -d '{
    "initialState": [
      [false, false, false, false, false],
      [false, false, true, false, false],
      [false, false, true, false, false],
      [false, false, true, false, false],
      [false, false, false, false, false]
    ],
    "name": "Blinker Pattern"
  }'
```

### Getting a Board by ID

```bash
curl -X GET http://localhost:8080/api/boards/1
```

### Computing the Next State

```bash
curl -X GET http://localhost:8080/api/boards/1/next
```

### Computing a State After Multiple Iterations

```bash
curl -X GET http://localhost:8080/api/boards/1/iterate/5
```

### Finding the Final State

```bash
curl -X GET http://localhost:8080/api/boards/1/final
```

## H2 Console

You can access the H2 database console at `http://localhost:8080/api/h2-console` with the following credentials:

- JDBC URL: `jdbc:h2:file:./data/gameoflife`
- Username: `sa`
- Password: `password`

## Persistence Mechanism

This implementation uses an H2 database with file-based storage to ensure that board states persist across application restarts or crashes. The key features of our persistence approach include:

1. Efficient storage of board states using bit-packing in a byte array
2. Automatic database schema creation and updates through Spring Data JPA
3. Transaction management to ensure data consistency
4. File-based storage that survives application restarts

## Troubleshooting

### Application Won't Start

- Ensure that port 8080 is available
- Check that you have write permissions in the application directory for the H2 database file
- Verify that Java 17 or higher is installed

### Database Issues

- If you encounter database errors, you can start with a fresh database by deleting the `./data` directory
- Ensure that the H2 database driver is in your classpath

## License

This project is licensed under the MIT License - see the LICENSE file for details.
