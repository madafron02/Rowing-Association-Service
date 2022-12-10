package nl.tudelft.sem.template.auth.application.handlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


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
    void testThrowError(){
        Exception exception = new RuntimeException();
        boolean bool = false;
        try {
            exceptionHandler.throwException(exception, "Test exception!");
            bool = true;
        } catch (Exception e){
            assertThat(e).isEqualTo(exception);
        }
        assertThat(bool).isFalse();
    }

    @Test
    void testGetMessage(){
        Exception exception = new RuntimeException();
        try {
            exceptionHandler.throwException(exception, "Test exception!");
        } catch (Exception e){

        }
        assertThat(exceptionHandler.getErrorMessage()).isEqualTo("Test exception!");
    }

    @Test
    void testGetException(){
        Exception exception = new RuntimeException();
        try {
            exceptionHandler.throwException(exception, "Test exception!");
        } catch (Exception e){

        }
        assertThat(exceptionHandler.getException()).isEqualTo(exception);
    }
}
