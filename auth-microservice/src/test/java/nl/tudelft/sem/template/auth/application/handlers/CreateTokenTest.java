package nl.tudelft.sem.template.auth.application.handlers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import nl.tudelft.sem.template.auth.domain.AccountCredentials;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.function.Function;

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
    void userIdTest() {
        createToken.handle(new AccountCredentials("hello", "world"));
        String token = createToken.getToken();
        Claims claims = Jwts.parser().setSigningKey("Secret123").parseClaimsJws(token).getBody();
        Function<Claims, String> claimsResolver = Claims::getSubject;
        String userId = claimsResolver.apply(claims);
        assertThat(userId).isEqualTo("hello");
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

    @Test
    void wrongSecretTest() {
        createToken.handle(new AccountCredentials("hello", "world"));
        String token = createToken.getToken();
        try {
            Claims claims = Jwts.parser().setSigningKey("WrongSecret").parseClaimsJws(token).getBody();
        } catch (Exception e) {
            assertThat(e).isInstanceOf(SignatureException.class);
            return;
        }
        assertThat(false).isTrue();
    }

    @Test
    void exceptionHandlerNullTest() {
        CreateToken createToken1 = new CreateToken("Secret");
        createToken1.handle(new AccountCredentials("hello", "world"));
        assertThat(createToken1.getToken()).isNull();
    }

    @Test
    void credentialsNullTest() {
        createToken.handle(null);
        assertThat(createToken.getToken()).isNull();
        assertThat(exceptionHandler.didCatchException()).isTrue();
    }


}
