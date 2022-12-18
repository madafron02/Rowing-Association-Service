package nl.tudelft.sem.template.auth.application.handlers;

import nl.tudelft.sem.template.auth.domain.AccountCredentials;
import nl.tudelft.sem.template.auth.domain.AccountsRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Tests for the VerifyCredentials handler.
 */
public class VerifyCredentialsTest {

    private VerifyCredentials verifyCredentials;
    private ExceptionHandler exceptionHandler;
    @Mock
    AuthHandler mockHandler;
    @Mock
    AccountsRepo mockRepo;

    @BeforeEach
    void setup() {
        mockRepo = mock(AccountsRepo.class);
        verifyCredentials = new VerifyCredentials(mockRepo);
        exceptionHandler = new ExceptionHandler();
        verifyCredentials.setExceptionHandler(exceptionHandler);
        mockHandler = mock(AuthHandler.class);
        verifyCredentials.setNext(mockHandler);
    }

    @Test
    void testConstructor() {
        assertThat(verifyCredentials).isNotNull();
    }

    @Test
    void testNewConstructor() {
        assertThat(new VerifyCredentials(mockRepo)).isNotNull();
    }

    @Test
    void testCorrectCredentials() {
        AccountCredentials credentials = new AccountCredentials("Foo", "Bar");
        PasswordEncoder encoder = new BCryptPasswordEncoder(12, new SecureRandom());
        AccountCredentials encoded = new AccountCredentials("Foo", encoder.encode("Bar"));
        Optional<AccountCredentials> found = Optional.of(encoded);
        when(mockRepo.findById(any())).thenReturn(found);

        verifyCredentials.handle(credentials);
        verify(mockHandler).handle(credentials);
        assertThat(exceptionHandler.didCatchException()).isFalse();
    }

    @Test
    void testIncorrectUserId() {
        AccountCredentials credentials = new AccountCredentials("Foo", "Bar");
        PasswordEncoder encoder = new BCryptPasswordEncoder(12, new SecureRandom());
        AccountCredentials encoded = new AccountCredentials("Baz", encoder.encode("Bar"));
        Optional<AccountCredentials> found = Optional.of(encoded);
        when(mockRepo.findById(any())).thenReturn(found);

        verifyCredentials.handle(credentials);
        verify(mockHandler, never()).handle(credentials);
        assertThat(exceptionHandler.didCatchException()).isTrue();
        assertThat(exceptionHandler.getErrorMessage())
                .isEqualTo("UserId or password incorrect. Please try again.");
    }

    @Test
    void testIncorrectPassword() {
        AccountCredentials credentials = new AccountCredentials("Foo", "Bar");
        PasswordEncoder encoder = new BCryptPasswordEncoder(12, new SecureRandom());
        AccountCredentials encoded = new AccountCredentials("Foo", encoder.encode("Baz"));
        Optional<AccountCredentials> found = Optional.of(encoded);
        when(mockRepo.findById(any())).thenReturn(found);

        verifyCredentials.handle(credentials);
        verify(mockHandler, never()).handle(credentials);
        assertThat(exceptionHandler.didCatchException()).isTrue();
        assertThat(exceptionHandler.getErrorMessage())
                .isEqualTo("UserId or password incorrect. Please try again.");
    }
}
