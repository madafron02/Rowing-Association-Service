package nl.tudelft.sem.template.auth.application.handlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
class ExceptionHandlerTest {

    private ExceptionHandler exceptionHandler;
    @BeforeEach
    void setUp(){
        exceptionHandler = new ExceptionHandler();
    }

    @Test
    void testConstructor() {
        assertThat(exceptionHandler).isNotNull();
    }

    @Test
    void testEmptyConstructor() {
        assertThat(new ExceptionHandler()).isNotNull();
    }

    @Test
    void testUnexpectedError(){
        assertThat(exceptionHandler.getErrorMessage()).isEqualTo("An unexpected error has occurred. Please try again.");
    }

    @Test
    void testNoException(){
        assertThat(exceptionHandler.didCatchException()).isFalse();
    }

    @Test
    void testException(){
        exceptionHandler.handleException(new RuntimeException());
        assertThat(exceptionHandler.didCatchException()).isTrue();
    }

    @Test
    void testGetException(){
        Exception exception = new RuntimeException();
        exceptionHandler.handleException(exception);
        assertThat(exceptionHandler.getException()).isEqualTo(exception);
    }

    @Test
    void testGetException2(){
        Exception exception = new RuntimeException();
        exceptionHandler.handleException(exception, "Test exception!");
        assertThat(exceptionHandler.getException()).isEqualTo(exception);
    }

    @Test
    void testGetMessage(){
        Exception exception = new RuntimeException();
        exceptionHandler.handleException(exception, "Test exception!");
        assertThat(exceptionHandler.getErrorMessage()).isEqualTo("Test exception!");
    }
}
