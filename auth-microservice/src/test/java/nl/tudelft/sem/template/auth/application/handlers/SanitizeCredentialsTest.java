package nl.tudelft.sem.template.auth.application.handlers;

import nl.tudelft.sem.template.auth.domain.AccountCredentials;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class SanitizeCredentialsTest {


    private SanitizeCredentials sanitizeCredentials;
    private ExceptionHandler exceptionHandler;

    @Mock SanitizeCredentials mockHandler;

    @BeforeEach
    void setUp(){
        sanitizeCredentials = new SanitizeCredentials();
        exceptionHandler = new ExceptionHandler();
        sanitizeCredentials.setExceptionHandler(exceptionHandler);
        mockHandler = mock(SanitizeCredentials.class);
        sanitizeCredentials.setNext(mockHandler);
    }

    @Test
    void testConstructor() {
        assertThat(sanitizeCredentials).isNotNull();
    }

    @Test
    void testEmptyConstructor() {
        assertThat(new SanitizeCredentials()).isNotNull();
    }

    @Test
    void testCallsNextTest(){
        sanitizeCredentials.handle(new AccountCredentials("hello.there@world.com", "world"));
        assertThat(exceptionHandler.didCatchException()).isFalse();
        verify(mockHandler, times(1)).handle(any());
    }

    @Test
    void testEmptyUserId(){
        sanitizeCredentials.handle(new AccountCredentials("", "world"));
        assertThat(exceptionHandler.didCatchException()).isTrue();
        verify(mockHandler, times(0)).handle(any());
    }

    @Test
    void testEmptyPassword(){
        sanitizeCredentials.handle(new AccountCredentials("hello@world.com", ""));
        assertThat(exceptionHandler.didCatchException()).isTrue();
        verify(mockHandler, times(0)).handle(any());
    }

    @Test
    void testIllegalUserId(){
        sanitizeCredentials.handle(new AccountCredentials("hello\"@world.com", "world"));
        assertThat(exceptionHandler.didCatchException()).isTrue();
        verify(mockHandler, times(0)).handle(any());
    }

    @Test
    void testNoEmail(){
        sanitizeCredentials.handle(new AccountCredentials("hello", "world"));
        assertThat(exceptionHandler.didCatchException()).isTrue();
        verify(mockHandler, times(0)).handle(any());
    }

    @Test
    void testIllegalPassword(){
        sanitizeCredentials.handle(new AccountCredentials("hello", "world]"));
        assertThat(exceptionHandler.didCatchException()).isTrue();
        verify(mockHandler, times(0)).handle(any());
    }

    @Test
    void testIncorrectEmail(){
        sanitizeCredentials.handle(new AccountCredentials("hello..there@world.com", "world"));
        assertThat(exceptionHandler.didCatchException()).isTrue();
        verify(mockHandler, times(0)).handle(any());
    }

}
