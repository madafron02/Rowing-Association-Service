package nl.tudelft.sem.template.auth.application.handlers;

import nl.tudelft.sem.template.auth.domain.AccountCredentials;
import nl.tudelft.sem.template.auth.domain.AccountsRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CreateAccountTest {


    private CreateAccount createAccount;
    private ExceptionHandler exceptionHandler;
    @Mock
    AuthHandler mockHandler;
    @Mock
    AccountsRepo mockRepo;

    @BeforeEach
    void setup(){
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
    void testEmptyConstructor() {
        assertThat(new CreateAccount(mockRepo)).isNotNull();
    }

    @Test
    void testAlreadyExists(){
        AccountCredentials credentials = new AccountCredentials("hello.there@world.com", "world");
        Optional<AccountCredentials> option = Optional.of(credentials);
        when(mockRepo.findById(any())).thenReturn(option);
        createAccount.handle(credentials);
        assertThat(exceptionHandler.didCatchException()).isTrue();
        assertThat(exceptionHandler.getErrorMessage()).isEqualTo("An account with this user id already exists." +
                " Please choose a different user id.");
        verify(mockHandler, times(0)).handle(any());
    }

    @Test
    void testFailsSaving(){
        AccountCredentials credentials = new AccountCredentials("hello.there@world.com", "world");
        Optional<AccountCredentials> option = Optional.empty();
        when(mockRepo.findById(any())).thenReturn(option);
        createAccount.handle(credentials);
        assertThat(exceptionHandler.didCatchException()).isTrue();
        assertThat(exceptionHandler.getErrorMessage()).isEqualTo("There was an error while saving your account." +
                " Please try again later");
        verify(mockHandler, times(0)).handle(any());
    }

}
