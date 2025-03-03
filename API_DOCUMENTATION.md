# Conway's Game of Life API Documentation

This document provides information about the Conway's Game of Life REST API.

## Base URL

```
http://localhost:8080/api
```

## Endpoints

### 1. Create a New Board

Creates a new Game of Life board with the provided initial state.

**URL**: `/boards`

**Method**: `POST`

**Content-Type**: `application/json`

**Request Body**:

```json
{
  "initialState": [
    [false, false, false, false, false],
    [false, false, true, false, false],
    [false, false, true, false, false],
    [false, false, true, false, false],
    [false, false, false, false, false]
  ],
  "name": "Blinker Pattern"
}
```

**Response**: `201 Created`

```json
{
  "id": 1,
  "state": [
    [false, false, false, false, false],
    [false, false, true, false, false],
    [false, false, true, false, false],
    [false, false, true, false, false],
    [false, false, false, false, false]
  ],
  "width": 5,
  "height": 5,
  "generation": 0,
  "createdAt": "2025-03-03T12:00:00.123",
  "updatedAt": "2025-03-03T12:00:00.123",
  "finalState": false,
  "liveCellCount": 3
}
```

### 2. Get a Board by ID

Retrieves a board by its ID.

**URL**: `/boards/{id}`

**Method**: `GET`

**Response**: `200 OK`

```json
{
  "id": 1,
  "state": [
    [false, false, false, false, false],
    [false, false, true, false, false],
    [false, false, true, false, false],
    [false, false, true, false, false],
    [false, false, false, false, false]
  ],
  "width": 5,
  "height": 5,
  "generation": 0,
  "createdAt": "2025-03-03T12:00:00.123",
  "updatedAt": "2025-03-03T12:00:00.123",
  "finalState": false,
  "liveCellCount": 3
}
```

### 3. Get Next State

Computes and returns the next state of a board.

**URL**: `/boards/{id}/next`

**Method**: `GET`

**Response**: `200 OK`

```json
{
  "id": 2,
  "state": [
    [false, false, false, false, false],
    [false, false, false, false, false],
    [false, true, true, true, false],
    [false, false, false, false, false],
    [false, false, false, false, false]
  ],
  "width": 5,
  "height": 5,
  "generation": 1,
  "createdAt": "2025-03-03T12:01:00.456",
  "updatedAt": "2025-03-03T12:01:00.456",
  "finalState": false,
  "liveCellCount": 3
}
```

### 4. Get State After Multiple Iterations

Computes a state that is a specific number of iterations away.

**URL**: `/boards/{id}/iterate/{iterations}`

**Method**: `GET`

**Response**: `200 OK`

```json
{
  "id": 3,
  "state": [
    [false, false, false, false, false],
    [false, false, true, false, false],
    [false, false, true, false, false],
    [false, false, true, false, false],
    [false, false, false, false, false]
  ],
  "width": 5,
  "height": 5,
  "generation": 2,
  "createdAt": "2025-03-03T12:02:00.789",
  "updatedAt": "2025-03-03T12:02:00.789",
  "finalState": true,
  "liveCellCount": 3
}
```

### 5. Get Final State

Computes the final state of a board (if it exists).

**URL**: `/boards/{id}/final`

**Method**: `GET`

**Response**: `200 OK`

```json
{
  "id": 4,
  "state": [
    [false, false, false, false, false],
    [false, false, true, false, false],
    [false, false, true, false, false],
    [false, false, true, false, false],
    [false, false, false, false, false]
  ],
  "width": 5,
  "height": 5,
  "generation": 2,
  "createdAt": "2025-03-03T12:03:00.012",
  "updatedAt": "2025-03-03T12:03:00.012",
  "finalState": true,
  "liveCellCount": 3
}
```

## Error Responses

### Board Not Found

**Status Code**: `404 Not Found`

```json
{
  "status": 404,
  "message": "Could not find board with id: 999",
  "timestamp": "2025-03-03T12:10:00.123"
}
```

### Board Processing Error

**Status Code**: `500 Internal Server Error`

```json
{
  "status": 500,
  "message": "Could not determine final state within 1000 iterations",
  "timestamp": "2025-03-03T12:11:00.456"
}
```

### Validation Error

**Status Code**: `400 Bad Request`

```json
{
  "status": 400,
  "message": "Validation error",
  "timestamp": "2025-03-03T12:12:00.789",
  "errors": {
    "initialState": "Initial board state is required"
  }
}
```

## Common Patterns

Here are some common Game of Life patterns you can use for testing:

### Blinker (Period 2 Oscillator)

```json
{
  "initialState": [
    [false, false, false, false, false],
    [false, false, false, false, false],
    [false, true, true, true, false],
    [false, false, false, false, false],
    [false, false, false, false, false]
  ],
  "name": "Blinker"
}
```

### Glider (Spaceship)

```json
{
  "initialState": [
    [false, false, false, false, false],
    [false, false, true, false, false],
    [false, false, false, true, false],
    [false, true, true, true, false],
    [false, false, false, false, false]
  ],
  "name": "Glider"
}
```

### Block (Still Life)

```json
{
  "initialState": [
    [false, false, false, false],
    [false, true, true, false],
    [false, true, true, false],
    [false, false, false, false]
  ],
  "name": "Block"
}
```
