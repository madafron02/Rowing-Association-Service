package nl.tudelft.sem.template.auth.application.handlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the ExceptionHandler.
 */
class ExceptionHandlerTest {

    private ExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new ExceptionHandler();
    }

    @Test
    void testConstructor() {
        assertThat(exceptionHandler).isNotNull();
    }

    @Test
    void testNewConstructor() {
        assertThat(new ExceptionHandler()).isNotNull();
    }

    @Test
    void testUnexpectedError() {
        assertThat(exceptionHandler.getErrorMessage()).isEqualTo("An unexpected error has occurred. Please try again.");
    }

    @Test
    void testNoException() {
        assertThat(exceptionHandler.didCatchException()).isFalse();
    }

    @Test
    void testException() {
        exceptionHandler.handleException(new RuntimeException());
        assertThat(exceptionHandler.didCatchException()).isTrue();
    }

    @Test
    void testGetException() {
        Exception exception = new RuntimeException();
        exceptionHandler.handleException(exception);
        assertThat(exceptionHandler.getException()).isEqualTo(exception);
    }

    @Test
    void testGetException2() {
        Exception exception = new RuntimeException();
        exceptionHandler.handleException(exception, "Test exception!", 400);
        assertThat(exceptionHandler.getException()).isEqualTo(exception);
    }

    @Test
    void testGetMessage() {
        Exception exception = new RuntimeException();
        exceptionHandler.handleException(exception, "Test exception!", 400);
        assertThat(exceptionHandler.getErrorMessage()).isEqualTo("Test exception!");
    }

    @Test
    void testDefaultStatus() {
        Exception exception = new RuntimeException();
        exceptionHandler.handleException(exception);
        assertThat(exceptionHandler.getStatusCode()).isEqualTo(500);
    }

    @Test
    void testCustomStatus() {
        Exception exception = new RuntimeException();
        exceptionHandler.handleException(exception, "Test exception!", 42);
        assertThat(exceptionHandler.getStatusCode()).isEqualTo(42);
    }
}
