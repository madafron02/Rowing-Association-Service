package nl.tudelft.sem.template.auth.controllers;

import nl.tudelft.sem.template.auth.application.handlers.CreateToken;
import nl.tudelft.sem.template.auth.application.handlers.ExceptionHandler;
import nl.tudelft.sem.template.auth.domain.AccountsRepo;
import nl.tudelft.sem.template.auth.domain.ChainCreator;
import nl.tudelft.sem.template.auth.models.CredentialsRequestModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for the AuthenticationController class.
 */
public class AuthenticationControllerTest {

    private AuthenticationController authenticationController;

    @Mock
    private AccountsRepo mockRepo;

    @Mock
    private CreateToken mockCreateToken;

    @BeforeEach
    void setUp() {
        mockRepo = mock(AccountsRepo.class);
        authenticationController = new AuthenticationController(mockRepo);
        mockCreateToken = mock(CreateToken.class);
    }

    @Test
    void testConstructor() {
        assertThat(authenticationController).isNotNull();
    }

    @Test
    void testCorrectRegistration() {
        try (MockedStatic<ChainCreator> mockChainCreator = Mockito.mockStatic(ChainCreator.class)) {
            CredentialsRequestModel request = new CredentialsRequestModel();
            request.setUserId("id");
            request.setPassword("pass");
            Exception exception = null;
            ResponseEntity response = null;
            mockChainCreator.when(() -> ChainCreator.createRegistrationChain(any(), any(), any(), any()))
                    .thenReturn(mockCreateToken);
            when(mockCreateToken.getToken()).thenReturn("This is a Token");

            MockedConstruction<ExceptionHandler> mockExceptionHandler = Mockito.mockConstruction(ExceptionHandler.class,
                    (mock, context) -> {
                        when(mock.didCatchException()).thenReturn(false);
                    });
            try {
                response = authenticationController.register(request);
            } catch (Exception e) {
                exception = e;
            }
            assertThat(mockExceptionHandler.constructed().isEmpty()).isFalse();
            mockExceptionHandler.close();
            assertThat(exception).isNull();
            assertThat(response).isEqualTo(ResponseEntity.ok("This is a Token"));
        }
    }

    @Test
    void testExceptionRegistration() {
        try (MockedStatic<ChainCreator> mockChainCreator = Mockito.mockStatic(ChainCreator.class)) {
            CredentialsRequestModel request = new CredentialsRequestModel();
            request.setUserId("id");
            request.setPassword("pass");
            Exception exception = null;
            ResponseEntity response = null;
            mockChainCreator.when(() -> ChainCreator.createRegistrationChain(any(), any(), any(), any()))
                    .thenReturn(mockCreateToken);
            when(mockCreateToken.getToken()).thenReturn("This is a Token");

            MockedConstruction<ExceptionHandler> mockExceptionHandler = Mockito.mockConstruction(ExceptionHandler.class,
                    (mock, context) -> {
                        when(mock.didCatchException()).thenReturn(true);
                        when(mock.getErrorMessage()).thenReturn("error");
                        when(mock.getStatusCode()).thenReturn(404);
                    });
            try {
                response = authenticationController.register(request);
            } catch (Exception e) {
                exception = e;
            }
            assertThat(mockExceptionHandler.constructed().isEmpty()).isFalse();
            mockExceptionHandler.close();
            assertThat(exception).isNull();
            assertThat(response).isEqualTo(ResponseEntity.status(404).body("error"));
        }
    }

    @Test
    void testNoTokenRegistration() {
        try (MockedStatic<ChainCreator> mockChainCreator = Mockito.mockStatic(ChainCreator.class)) {
            CredentialsRequestModel request = new CredentialsRequestModel();
            request.setUserId("id");
            request.setPassword("pass");
            Exception exception = null;
            ResponseEntity response = null;
            mockChainCreator.when(() -> ChainCreator.createRegistrationChain(any(), any(), any(), any()))
                    .thenReturn(mockCreateToken);
            when(mockCreateToken.getToken()).thenReturn(null);

            MockedConstruction<ExceptionHandler> mockExceptionHandler = Mockito.mockConstruction(ExceptionHandler.class,
                    (mock, context) -> {
                        when(mock.didCatchException()).thenReturn(false);
                        when(mock.getErrorMessage()).thenReturn("Unexpected error");
                        when(mock.getStatusCode()).thenReturn(500);
                    });
            try {
                response = authenticationController.register(request);
            } catch (Exception e) {
                exception = e;
            }
            assertThat(mockExceptionHandler.constructed().isEmpty()).isFalse();
            mockExceptionHandler.close();
            assertThat(exception).isNull();
            assertThat(response).isEqualTo(ResponseEntity.status(500).body("Unexpected error"));
        }
    }

    @Test
    void testCorrectAuthentication() {
        try (MockedStatic<ChainCreator> mockChainCreator = Mockito.mockStatic(ChainCreator.class)) {
            CredentialsRequestModel request = new CredentialsRequestModel();
            request.setUserId("Foo");
            request.setPassword("Bar");
            Exception exception = null;
            ResponseEntity response = null;
            mockChainCreator.when(() -> ChainCreator.createRegistrationChain(any(), any(), any(), any()))
                    .thenReturn(mockCreateToken);
            when(mockCreateToken.getToken()).thenReturn("This is a Token");

            MockedConstruction<ExceptionHandler> mockExceptionHandler = Mockito.mockConstruction(ExceptionHandler.class,
                    (mock, context) -> {
                        when(mock.didCatchException()).thenReturn(false);
                    });
            try {
                response = authenticationController.register(request);
            } catch (Exception e) {
                exception = e;
            }
            assertThat(mockExceptionHandler.constructed().isEmpty()).isFalse();
            mockExceptionHandler.close();
            assertThat(exception).isNull();
            assertThat(response).isEqualTo(ResponseEntity.ok("This is a Token"));
        }
    }

    @Test
    void testExceptionAuthentication() {
        try (MockedStatic<ChainCreator> mockChainCreator = Mockito.mockStatic(ChainCreator.class)) {
            CredentialsRequestModel request = new CredentialsRequestModel();
            request.setUserId("Foo");
            request.setPassword("Bar");
            Exception exception = null;
            ResponseEntity response = null;
            mockChainCreator.when(() -> ChainCreator.createRegistrationChain(any(), any(), any(), any()))
                    .thenReturn(mockCreateToken);
            when(mockCreateToken.getToken()).thenReturn("This is a Token");

            MockedConstruction<ExceptionHandler> mockExceptionHandler = Mockito.mockConstruction(ExceptionHandler.class,
                    (mock, context) -> {
                        when(mock.didCatchException()).thenReturn(true);
                        when(mock.getErrorMessage()).thenReturn("error");
                        when(mock.getStatusCode()).thenReturn(404);
                    });
            try {
                response = authenticationController.register(request);
            } catch (Exception e) {
                exception = e;
            }
            assertThat(mockExceptionHandler.constructed().isEmpty()).isFalse();
            mockExceptionHandler.close();
            assertThat(exception).isNull();
            assertThat(response).isEqualTo(ResponseEntity.status(404).body("error"));
        }
    }

    @Test
    void testNoTokenAuthentication() {
        try (MockedStatic<ChainCreator> mockChainCreator = Mockito.mockStatic(ChainCreator.class)) {
            CredentialsRequestModel request = new CredentialsRequestModel();
            request.setUserId("Foo");
            request.setPassword("Bar");
            Exception exception = null;
            ResponseEntity response = null;
            mockChainCreator.when(() -> ChainCreator.createRegistrationChain(any(), any(), any(), any()))
                    .thenReturn(mockCreateToken);
            when(mockCreateToken.getToken()).thenReturn(null);

            MockedConstruction<ExceptionHandler> mockExceptionHandler = Mockito.mockConstruction(ExceptionHandler.class,
                    (mock, context) -> {
                        when(mock.didCatchException()).thenReturn(false);
                        when(mock.getErrorMessage()).thenReturn("Unexpected error");
                        when(mock.getStatusCode()).thenReturn(500);
                    });
            try {
                response = authenticationController.register(request);
            } catch (Exception e) {
                exception = e;
            }
            assertThat(mockExceptionHandler.constructed().isEmpty()).isFalse();
            mockExceptionHandler.close();
            assertThat(exception).isNull();
            assertThat(response).isEqualTo(ResponseEntity.status(500).body("Unexpected error"));
        }
    }
}
