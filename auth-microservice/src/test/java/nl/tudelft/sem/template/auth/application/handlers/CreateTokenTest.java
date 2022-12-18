package nl.tudelft.sem.template.auth.application.handlers;

import nl.tudelft.sem.template.auth.domain.AccountCredentials;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for the CreateToken handler.
 */
public class CreateTokenTest {

    private CreateToken createToken;
    private ExceptionHandler exceptionHandler;
    @Mock
    AuthHandler mockHandler;

    @BeforeEach
    void setup() {
        createToken = new CreateToken("Secret123");
        exceptionHandler = new ExceptionHandler();
        createToken.setExceptionHandler(exceptionHandler);
        mockHandler = mock(AuthHandler.class);
        createToken.setNext(mockHandler);
    }

    @Test
    void testConstructor() {
        assertThat(createToken).isNotNull();
    }

    @Test
    void testNewConstructor() {
        assertThat(new CreateToken("Secret123")).isNotNull();
    }

    @Test
    void testTokenCreation() {
        createToken.handle(new AccountCredentials("hello", "world"));
        String token = createToken.getToken();
        assertThat(token).isNotNull();
    }

    @Test
    void testNoExceptions() {
        createToken.handle(new AccountCredentials("hello", "world"));
        assertThat(exceptionHandler.didCatchException()).isFalse();
    }

    @Test
    void testTokenCreation2() {
        createToken.handle(new AccountCredentials("hello", "world"));
        String token = createToken.getToken();
        assertThat(token.substring(0, 20)).isEqualTo("eyJhbGciOiJIUzUxMiJ9");
    }
}
