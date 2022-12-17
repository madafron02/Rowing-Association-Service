package nl.tudelft.sem.template.auth.domain;

import nl.tudelft.sem.template.auth.application.handlers.CreateAccount;
import nl.tudelft.sem.template.auth.application.handlers.CreateToken;
import nl.tudelft.sem.template.auth.application.handlers.ExceptionHandler;
import nl.tudelft.sem.template.auth.application.handlers.SanitizeCredentials;
import nl.tudelft.sem.template.auth.application.handlers.VerifyCredentials;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Tests for the ChainCreator class.
 */
public class ChainCreatorTest {

    private ExceptionHandler exceptionHandler;
    private String testSecret;
    private AccountCredentials credentials;
    @Mock
    AccountsRepo mockRepo;



    @BeforeEach
    void setup() {
        exceptionHandler = new ExceptionHandler();
        mockRepo = mock(AccountsRepo.class);
        testSecret = "test123";
        credentials = new AccountCredentials("hello.there@world.com", "world");
    }

    @Test
    void testCorrectRegistrationChain() {
        MockedConstruction<SanitizeCredentials> mockedSanitizeCredentials =
                Mockito.mockConstruction(SanitizeCredentials.class,
                    (mock, context) -> {
                        doNothing().when(mock).setNext(any());
                        doNothing().when(mock).setExceptionHandler(any());
                        doNothing().when(mock).handle(any());
                    });
        MockedConstruction<CreateAccount> mockedCreateAccount = Mockito.mockConstruction(CreateAccount.class,
                (mock, context) -> {
                    doNothing().when(mock).setNext(any());
                    doNothing().when(mock).setExceptionHandler(any());
                    doNothing().when(mock).handle(any());
                });
        MockedConstruction<CreateToken> mockedCreateToken = Mockito.mockConstruction(CreateToken.class,
                (mock, context) -> {
                    doNothing().when(mock).setNext(any());
                    doNothing().when(mock).setExceptionHandler(any());
                    doNothing().when(mock).handle(any());
                    when(mock.getToken()).thenReturn("this is the token");
                });

        CreateToken returnedHandler = ChainCreator.createRegistrationChain(exceptionHandler, mockRepo, testSecret,
                credentials);

        assertThat(mockedSanitizeCredentials.constructed().isEmpty()).isFalse();
        assertThat(mockedCreateAccount.constructed().isEmpty()).isFalse();
        assertThat(mockedCreateToken.constructed().isEmpty()).isFalse();
        verify(mockedSanitizeCredentials.constructed().get(0), times(1))
                .setNext(mockedCreateAccount.constructed().get(0));
        verify(mockedCreateAccount.constructed().get(0), times(1))
                .setNext(mockedCreateToken.constructed().get(0));
        verify(mockedSanitizeCredentials.constructed().get(0), times(1))
                .setExceptionHandler(exceptionHandler);
        verify(mockedCreateAccount.constructed().get(0), times(1))
                .setExceptionHandler(exceptionHandler);
        verify(mockedCreateToken.constructed().get(0), times(1))
                .setExceptionHandler(exceptionHandler);
        verify(mockedSanitizeCredentials.constructed().get(0), times(1))
                .handle(credentials);
        assertThat(returnedHandler).isEqualTo(mockedCreateToken.constructed().get(0));

        mockedSanitizeCredentials.close();
        mockedCreateAccount.close();
        mockedCreateToken.close();
    }

    @Test
    void testCorrectAuthenticationChain() {
        MockedConstruction<SanitizeCredentials> mockedSanitizeCredentials =
                Mockito.mockConstruction(SanitizeCredentials.class,
                        (mock, context) -> {
                            doNothing().when(mock).setNext(any());
                            doNothing().when(mock).setExceptionHandler(any());
                            doNothing().when(mock).handle(any());
                        });
        MockedConstruction<VerifyCredentials> mockedVerifyCredentials = Mockito.mockConstruction(VerifyCredentials.class,
                (mock, context) -> {
                    doNothing().when(mock).setNext(any());
                    doNothing().when(mock).setExceptionHandler(any());
                    doNothing().when(mock).handle(any());
                });
        MockedConstruction<CreateToken> mockedCreateToken = Mockito.mockConstruction(CreateToken.class,
                (mock, context) -> {
                    doNothing().when(mock).setNext(any());
                    doNothing().when(mock).setExceptionHandler(any());
                    doNothing().when(mock).handle(any());
                    when(mock.getToken()).thenReturn("this is the token");
                });

        CreateToken returnedHandler = ChainCreator.createAuthenticationChain(exceptionHandler, mockRepo, testSecret,
                credentials);

        assertThat(mockedSanitizeCredentials.constructed().isEmpty()).isFalse();
        assertThat(mockedVerifyCredentials.constructed().isEmpty()).isFalse();
        assertThat(mockedCreateToken.constructed().isEmpty()).isFalse();
        verify(mockedSanitizeCredentials.constructed().get(0), times(1))
                .setNext(mockedVerifyCredentials.constructed().get(0));
        verify(mockedVerifyCredentials.constructed().get(0), times(1))
                .setNext(mockedCreateToken.constructed().get(0));
        verify(mockedSanitizeCredentials.constructed().get(0), times(1))
                .setExceptionHandler(exceptionHandler);
        verify(mockedVerifyCredentials.constructed().get(0), times(1))
                .setExceptionHandler(exceptionHandler);
        verify(mockedCreateToken.constructed().get(0), times(1))
                .setExceptionHandler(exceptionHandler);
        verify(mockedSanitizeCredentials.constructed().get(0), times(1))
                .handle(credentials);
        assertThat(returnedHandler).isEqualTo(mockedCreateToken.constructed().get(0));
        mockedSanitizeCredentials.close();
        mockedVerifyCredentials.close();
        mockedCreateToken.close();
    }
}
