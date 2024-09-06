package com.orhanararat.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RuleBuildExceptionTest {

    @Test
    void constructor_ShouldSetMessage() {
        // Given
        String errorMessage = "Error occurred while building the rule";

        // When
        RuleBuildException exception = new RuleBuildException(errorMessage);

        // Then
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void exceptionType_ShouldBeRuntimeException() {
        // Given
        RuleBuildException exception = new RuleBuildException("Test message");

        // Then
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void constructor_WithNullMessage_ShouldNotThrowException() {
        // When/Then
        assertDoesNotThrow(() -> new RuleBuildException(null));
    }
}