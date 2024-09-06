package com.orhanararat.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleResourceNotFoundException_ShouldReturnBadRequest() {
        // Given
        NonUniquePriorityException exception = new NonUniquePriorityException("Priority must be unique");

        // When
        ResponseEntity<?> responseEntity = exceptionHandler.handleResourceNotFoundException(exception);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof ErrorResponse);
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertEquals(HttpStatus.BAD_REQUEST, errorResponse.getStatus());
        assertEquals("Priority must be unique", errorResponse.getMessage());
    }

    @Test
    void handleGlobalException_ShouldReturnInternalServerError() {
        // Given
        Exception exception = new Exception("Unexpected error occurred");

        // When
        ResponseEntity<?> responseEntity = exceptionHandler.handleGlobalException(exception);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof ErrorResponse);
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, errorResponse.getStatus());
        assertEquals("Unexpected error occurred", errorResponse.getMessage());
    }

    @Test
    void handleRuleBuildException_ShouldReturnBadRequest() {
        // Given
        RuleBuildException exception = new RuleBuildException("Error building rule");

        // When
        ResponseEntity<?> responseEntity = exceptionHandler.handleRuleBuildException(exception);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof ErrorResponse);
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertEquals(HttpStatus.BAD_REQUEST, errorResponse.getStatus());
        assertEquals("Error building rule", errorResponse.getMessage());
    }
}