package nl.tudelft.sem.template.users.domain.handlers;

import nl.tudelft.sem.template.users.domain.User;
import nl.tudelft.sem.template.users.domain.database.OrganisationRepo;
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
public class OrganisationValidationHandlerTests {

    private OrganisationRepo organisationRepo;

    private UserValidationHandler userValidationHandler;

    private OrganisationValidationHandler organisationValidationHandler;

    @BeforeEach
    void setUp() {
        organisationRepo = Mockito.mock(OrganisationRepo.class);
        userValidationHandler = Mockito.mock(UserValidationHandler.class);
        organisationValidationHandler = new OrganisationValidationHandler(organisationRepo);
    }

    @Test
    public void nullOrganisationNextTest() {
        User testUser = new User("abc", null, false, null, null);
        organisationValidationHandler.setNext(userValidationHandler);
        when(userValidationHandler.handle(testUser)).thenReturn(true);

        assertThat(organisationValidationHandler.handle(testUser)).isTrue();
        verify(userValidationHandler, times(1)).handle(testUser);
        verify(organisationRepo, never()).existsOrganisationByName(any());
    }

    @Test
    public void validOrganisationEndChainTest() {
        User testUser = new User("abc", null, false, null, "Proteus");
        when(organisationRepo.existsOrganisationByName("Proteus")).thenReturn(true);

        assertThat(organisationValidationHandler.handle(testUser)).isTrue();
    }

    @Test
    public void invalidCertificateTest() {
        User testUser = new User("abc", null, false, null, "totallyLegit");
        organisationValidationHandler.setNext(userValidationHandler);

        when(organisationRepo.existsOrganisationByName("totallyLegit")).thenReturn(false);

        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            organisationValidationHandler.handle(testUser);
        });

        assertThat(e.getMessage()).isEqualTo("This organisation is not recognised by our system.");
        verify(userValidationHandler, never()).handle(any());
    }
}
