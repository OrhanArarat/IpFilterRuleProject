package com.orhanararat.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NonUniquePriorityExceptionTest {

    @Test
    void constructor_ShouldSetMessage() {
        // Given
        String errorMessage = "Priority must be unique";

        // When
        NonUniquePriorityException exception = new NonUniquePriorityException(errorMessage);

        // Then
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void exceptionType_ShouldBeRuntimeException() {
        // Given
        NonUniquePriorityException exception = new NonUniquePriorityException("Test message");

        // Then
        assertTrue(exception instanceof RuntimeException);
    }
}