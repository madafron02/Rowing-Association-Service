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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Tests for the CreateAccount handler.
 */
public class CreateAccountTest {


    private CreateAccount createAccount;
    private ExceptionHandler exceptionHandler;
    @Mock
    AuthHandler mockHandler;
    @Mock
    AccountsRepo mockRepo;

    @BeforeEach
    void setup() {
        mockRepo = mock(AccountsRepo.class);
        createAccount = new CreateAccount(mockRepo);
        exceptionHandler = new ExceptionHandler();
        createAccount.setExceptionHandler(exceptionHandler);
        mockHandler = mock(AuthHandler.class);
        createAccount.setNext(mockHandler);
    }

    @Test
    void testConstructor() {
        assertThat(createAccount).isNotNull();
    }

    @Test
    void testNewConstructor() {
        assertThat(new CreateAccount(mockRepo)).isNotNull();
    }

    @Test
    void testCorrectCreating() {
        AccountCredentials credentials = new AccountCredentials("hello.there@world.com", "world");
        PasswordEncoder encoder = new BCryptPasswordEncoder(12, new SecureRandom());
        AccountCredentials encoded = new AccountCredentials("hello.there@world.com",
                encoder.encode("world"));
        Optional<AccountCredentials> notFound = Optional.empty();
        Optional<AccountCredentials> found = Optional.of(encoded);
        when(mockRepo.findById(any())).thenReturn(notFound).thenReturn(found);
        createAccount.handle(credentials);
        verify(mockRepo, times(2)).findById(any());
        verify(mockRepo, times(1)).save(any(AccountCredentials.class));
        verify(mockHandler, times(1)).handle(any());
    }

    @Test
    void testAlreadyExists() {
        AccountCredentials credentials = new AccountCredentials("hello.there@world.com", "world");
        Optional<AccountCredentials> option = Optional.of(credentials);
        when(mockRepo.findById(any())).thenReturn(option);
        createAccount.handle(credentials);
        assertThat(exceptionHandler.didCatchException()).isTrue();
        assertThat(exceptionHandler.getErrorMessage()).isEqualTo("An account with this user id already exists."
                + " Please choose a different user id.");
        verify(mockHandler, times(0)).handle(any());
    }

    @Test
    void testFailsSaving() {
        AccountCredentials credentials = new AccountCredentials("hello.there@world.com", "world");
        Optional<AccountCredentials> option = Optional.empty();
        when(mockRepo.findById(any())).thenReturn(option);
        when(mockRepo.save(any())).thenThrow(new RuntimeException());
        createAccount.handle(credentials);
        assertThat(exceptionHandler.didCatchException()).isTrue();
        verify(mockHandler, times(0)).handle(any());
    }

    @Test
    void testNotSavedCorrectly() {
        AccountCredentials credentials = new AccountCredentials("hello.there@world.com", "world");
        Optional<AccountCredentials> option = Optional.empty();
        when(mockRepo.findById(any())).thenReturn(option).thenReturn(option);
        when(mockRepo.save(any())).thenReturn(credentials);
        createAccount.handle(credentials);
        assertThat(exceptionHandler.didCatchException()).isTrue();
        assertThat(exceptionHandler.getErrorMessage()).isEqualTo("There was an error while saving your account."
                + " Please try again later");
        verify(mockHandler, times(0)).handle(any());
    }

    @Test
    void nextNullTest() {
        CreateAccount createAccount1 = new CreateAccount(mockRepo);
        createAccount1.setExceptionHandler(exceptionHandler);
        AccountCredentials credentials = new AccountCredentials("hello.there@world.com", "world");
        createAccount1.handle(credentials);
        verify(mockRepo, times(0)).findById(any());
    }

    @Test
    void exceptionHandlerNullTest() {
        CreateAccount createAccount1 = new CreateAccount(mockRepo);
        createAccount1.setNext(mockHandler);
        AccountCredentials credentials = new AccountCredentials("hello.there@world.com", "world");
        createAccount1.handle(credentials);
        verify(mockRepo, times(0)).findById(any());
    }
}
