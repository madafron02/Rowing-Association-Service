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
public class GenderValidationHandlerTests {

    private UserValidationHandler userValidationHandler;

    private GenderValidationHandler genderValidationHandler;

    @BeforeEach
    void setUp() {
        userValidationHandler = Mockito.mock(UserValidationHandler.class);
        genderValidationHandler = new GenderValidationHandler();
    }

    @Test
    public void validGenderNextTest() {
        User testUser = new User("abc@def.com", "Male", false, null, null);
        genderValidationHandler.setNext(userValidationHandler);
        when(userValidationHandler.handle(testUser)).thenReturn(true);

        assertThat(genderValidationHandler.handle(testUser)).isTrue();
        verify(userValidationHandler, times(1)).handle(testUser);
    }

    @Test
    public void validGenderEndChainTest() {
        User testUser = new User("abc@def.com", "Female", false, null, null);

        assertThat(genderValidationHandler.handle(testUser)).isTrue();
        verify(userValidationHandler, never()).handle(testUser);
    }

    @Test
    public void nullGenderTest() {
        User testUser = new User("a.b@c.com", null, false, null, null);

        assertThat(genderValidationHandler.handle(testUser)).isTrue();
        verify(userValidationHandler, never()).handle(testUser);
    }

    @Test
    public void invalidGenderTest() {
        User testUser = new User("blabla", "notAGender", false, null, null);

        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            genderValidationHandler.handle(testUser);
        });

        assertThat(e.getMessage()).isEqualTo("Please enter your gender as 'Male' or 'Female'");
    }

    @Test
    public void caseSensitiveTest() {
        User testUser = new User("blabla", "female", false, null, null);

        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            genderValidationHandler.handle(testUser);
        });

        assertThat(e.getMessage()).isEqualTo("Please enter your gender as 'Male' or 'Female'");
    }
}