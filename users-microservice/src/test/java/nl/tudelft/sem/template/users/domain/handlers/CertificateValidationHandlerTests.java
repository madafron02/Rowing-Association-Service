package nl.tudelft.sem.template.users.domain.handlers;

import nl.tudelft.sem.template.users.application.MatchingCommunication;
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
import static org.mockito.Mockito.any;


@SpringBootTest
public class CertificateValidationHandlerTests {

    private MatchingCommunication matchingCommunication;

    private UserValidationHandler userValidationHandler;

    private CertificateValidationHandler certificateValidationHandler;

    @BeforeEach
    void setUp() {
        matchingCommunication = Mockito.mock(MatchingCommunication.class);
        userValidationHandler = Mockito.mock(UserValidationHandler.class);
        certificateValidationHandler = new CertificateValidationHandler(matchingCommunication);
    }

    @Test
    public void nullCertificateNextTest() {
        User testUser = new User("abc", null, false, null, null);
        certificateValidationHandler.setNext(userValidationHandler);
        when(userValidationHandler.handle(testUser)).thenReturn(true);

        assertThat(certificateValidationHandler.handle(testUser)).isTrue();
        verify(userValidationHandler, times(1)).handle(testUser);
        verify(matchingCommunication, never()).validateCertificate(any());
    }

    @Test
    public void validCertificateEndChainTest() {
        User testUser = new User("abc", null, false, "4+", null);
        when(matchingCommunication.validateCertificate("4+")).thenReturn(true);

        assertThat(certificateValidationHandler.handle(testUser)).isTrue();
    }

    @Test
    public void invalidCertificateTest() {
        User testUser = new User("abc", null, false, "trustme", null);
        certificateValidationHandler.setNext(userValidationHandler);

        when(matchingCommunication.validateCertificate("trustme")).thenReturn(false);

        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            certificateValidationHandler.handle(testUser);
        });

        assertThat(e.getMessage()).isEqualTo("This certificate is not recognized by our system.");
        verify(userValidationHandler, never()).handle(any());
    }
}
