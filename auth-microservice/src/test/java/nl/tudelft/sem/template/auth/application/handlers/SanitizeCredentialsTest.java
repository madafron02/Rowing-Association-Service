package nl.tudelft.sem.template.auth.application.handlers;

import nl.tudelft.sem.template.auth.domain.AccountCredentials;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests for the SanitizeCredentials handler.
 */
public class SanitizeCredentialsTest {


    private SanitizeCredentials sanitizeCredentials;
    private ExceptionHandler exceptionHandler;

    @Mock
    AuthHandler mockHandler;

    @BeforeEach
    void setUp() {
        sanitizeCredentials = new SanitizeCredentials();
        exceptionHandler = new ExceptionHandler();
        sanitizeCredentials.setExceptionHandler(exceptionHandler);
        mockHandler = mock(AuthHandler.class);
        sanitizeCredentials.setNext(mockHandler);
    }

    @Test
    void testConstructor() {
        assertThat(sanitizeCredentials).isNotNull();
    }

    @Test
    void testNewConstructor() {
        assertThat(new SanitizeCredentials()).isNotNull();
    }

    @Test
    void testCallsNext() {
        sanitizeCredentials.handle(new AccountCredentials("hello.there@world.com", "world"));
        assertThat(exceptionHandler.didCatchException()).isFalse();
        verify(mockHandler, times(1)).handle(any());
    }

    @Test
    void testEmptyUserId() {
        sanitizeCredentials.handle(new AccountCredentials("", "world"));
        assertThat(exceptionHandler.didCatchException()).isTrue();
        assertThat(exceptionHandler.getErrorMessage()).isEqualTo("Please provide a user id.");
        verify(mockHandler, times(0)).handle(any());
    }

    @Test
    void testNullUserId() {
        sanitizeCredentials.handle(new AccountCredentials(null, "world"));
        assertThat(exceptionHandler.didCatchException()).isTrue();
        assertThat(exceptionHandler.getErrorMessage()).isEqualTo("Please provide a user id.");
        verify(mockHandler, times(0)).handle(any());
    }

    @Test
    void testNullPassword() {
        sanitizeCredentials.handle(new AccountCredentials("hello@world.com", null));
        assertThat(exceptionHandler.didCatchException()).isTrue();
        assertThat(exceptionHandler.getErrorMessage()).isEqualTo("Please provide a password.");
        verify(mockHandler, times(0)).handle(any());
    }

    @Test
    void testEmptyPassword() {
        sanitizeCredentials.handle(new AccountCredentials("hello@world.com", ""));
        assertThat(exceptionHandler.didCatchException()).isTrue();
        assertThat(exceptionHandler.getErrorMessage()).isEqualTo("Please provide a password.");
        verify(mockHandler, times(0)).handle(any());
    }

    @Test
    void testIllegalUserId() {
        sanitizeCredentials.handle(new AccountCredentials("hello\"@world.com", "world"));
        assertThat(exceptionHandler.didCatchException()).isTrue();
        assertThat(exceptionHandler.getErrorMessage()).isEqualTo("Your user id contains illegal"
                + " characters. Please only use letters, numbers and the following characters: "
                + "!#$%&()*+,-./:;<=>?@^_`{|}~");
        verify(mockHandler, times(0)).handle(any());
    }

    @Test
    void testNoEmail() {
        sanitizeCredentials.handle(new AccountCredentials("hello", "world"));
        assertThat(exceptionHandler.didCatchException()).isTrue();
        assertThat(exceptionHandler.getErrorMessage()).isEqualTo("Your user id must be an email address.");
        verify(mockHandler, times(0)).handle(any());
    }

    @Test
    void testIllegalPassword() {
        sanitizeCredentials.handle(new AccountCredentials("hello", "world]"));
        assertThat(exceptionHandler.didCatchException()).isTrue();
        assertThat(exceptionHandler.getErrorMessage()).isEqualTo("Your password contains illegal"
                + " characters. Please only use letters, numbers and the following characters: "
                + "!#$%&()*+,-./:;<=>?@^_`{|}~");
        verify(mockHandler, times(0)).handle(any());
    }

    @Test
    void testIncorrectEmail() {
        sanitizeCredentials.handle(new AccountCredentials("hello..there@world.com", "world"));
        assertThat(exceptionHandler.didCatchException()).isTrue();
        assertThat(exceptionHandler.getErrorMessage()).isEqualTo("Your user id must be an email address.");
        verify(mockHandler, times(0)).handle(any());
    }

    @Test
    void nextNullTest() {
        SanitizeCredentials sanitizeCredentials1 = new SanitizeCredentials();
        sanitizeCredentials1.setExceptionHandler(exceptionHandler);
        AccountCredentials mockCredentials = mock(AccountCredentials.class);
        sanitizeCredentials1.handle(mockCredentials);
        verify(mockCredentials, times(0)).getUserId();
    }

    @Test
    void exceptionHandlerNullTest() {
        SanitizeCredentials sanitizeCredentials1 = new SanitizeCredentials();
        sanitizeCredentials1.setNext(mockHandler);
        AccountCredentials mockCredentials = mock(AccountCredentials.class);
        sanitizeCredentials1.handle(mockCredentials);
        verify(mockCredentials, times(0)).getUserId();
    }

}
