package nl.tudelft.sem.template.users.domain.handlers;

import nl.tudelft.sem.template.users.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;


@SpringBootTest
public class EmailValidationHandlerTests {

    private UserValidationHandler userValidationHandler;

    private EmailValidationHandler emailValidationHandler;

    @BeforeEach
    void setUp() {
        userValidationHandler = Mockito.mock(UserValidationHandler.class);
        emailValidationHandler = new EmailValidationHandler();
    }

    @Test
    public void validEmailNextTest() {
        User testUser = new User("abc@def.com", null, false, null, null);
        emailValidationHandler.setNext(userValidationHandler);
        when(userValidationHandler.handle(testUser)).thenReturn(true);

        assertThat(emailValidationHandler.handle(testUser)).isTrue();
        verify(userValidationHandler, times(1)).handle(testUser);
    }

    @Test
    public void validEmailEndChainTest() {
        User testUser = new User("abc@def.com", null, false, null, null);

        assertThat(emailValidationHandler.handle(testUser)).isTrue();
        verify(userValidationHandler, never()).handle(testUser);
    }

    @Test
    public void nullEmailTest() {
        User testUser = new User(null, null, false, null, null);

        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            emailValidationHandler.handle(testUser);
        });

        assertThat(e.getMessage()).isEqualTo("This is not a valid email.");
        verify(userValidationHandler, never()).handle(testUser);
    }

    @Test
    public void invalidEmailTest() {
        User testUser = new User("blabla", null, false, null, null);

        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            emailValidationHandler.handle(testUser);
        });

        assertThat(e.getMessage()).isEqualTo("This is not a valid email.");
    }

    @Test
    public void validEmailsTest() {
        assertThat(emailValidationHandler.validateEmail("a.b@c.d")).isTrue();
        assertThat(emailValidationHandler.validateEmail("abc@c.d")).isTrue();
        assertThat(emailValidationHandler.validateEmail("a.b.c@c.d")).isTrue();
        assertThat(emailValidationHandler.validateEmail("abc!@c.d")).isTrue();
    }

    @Test
    public void invalidEmailsTest() {
        assertThat(emailValidationHandler.validateEmail("a.b@@c.d")).isFalse();
        assertThat(emailValidationHandler.validateEmail("abc@cd")).isFalse();
        assertThat(emailValidationHandler.validateEmail("a..bc@c.d")).isFalse();
        assertThat(emailValidationHandler.validateEmail("@c.d")).isFalse();
    }
}