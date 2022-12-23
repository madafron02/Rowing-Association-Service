package nl.tudelft.sem.template.users.domain;


import nl.tudelft.sem.template.users.application.MatchingCommunication;
import nl.tudelft.sem.template.users.domain.database.OrganisationRepo;
import nl.tudelft.sem.template.users.domain.database.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;



@SpringBootTest
public class UserServiceTest {

    @MockBean
    private UserRepo userRepo;

    @MockBean
    private OrganisationRepo organisationRepo;

    @MockBean
    private MatchingCommunication matchingCommunication;

    @Autowired
    private UserService service;

    /**
     * Sets up pre-defined return values.
     */
    @BeforeEach
    public void setUp() {
        when(matchingCommunication.validateCertificate("4+")).thenReturn(true);
        when(matchingCommunication.validateCertificate("8+")).thenReturn(true);

        when(organisationRepo.existsOrganisationByName("ProRow")).thenReturn(true);
        when(organisationRepo.existsOrganisationByName("Laga")).thenReturn(true);
    }

    @Test
    public void validationChainTest() {
        assertThat(service.userValidationHandler).isNotNull();
    }

    @Test
    public void getByEmailTest() {
        when(userRepo.existsUserByEmail("ealstad@tudelft.nl")).thenReturn(true);
        when(userRepo.getUserByEmail("ealstad@tudelft.nl")).thenReturn(new User("ealstad@tudelft.nl",
                null, false, null, null));
        assertThat(service.getByEmail("ealstad@tudelft.nl")).isEqualTo(new User("ealstad@tudelft.nl",
                null, false, null, null));
    }

    @Test
    public void getByInvalidEmailTest() {
        when(userRepo.existsUserByEmail("ealstad@tudelft.nl")).thenReturn(false);
        assertThat(service.getByEmail("ealstad@tudelft.nl")).isNull();
    }

    @Test
    public void saveValidUserTest() {
        User testUser = new User("a@b.com", null, false, null, null);
        assertThat(service.saveUser(testUser)).isEqualTo(testUser);

        verify(userRepo, times(1)).save(testUser);
    }

    @Test
    public void saveInvalidUserTest() {
        User testUser = new User("invalidEmail", null, false, null, null);
        assertThrows(IllegalArgumentException.class, () -> {
            assertThat(service.saveUser(testUser)).isNull();
        });
        verify(userRepo, never()).save(testUser);
    }

    @Test
    public void userExistsTest() {
        User testUser = new User("a@b.com", null, false, null, null);
        when(userRepo.existsUserByEmail("a@b.com")).thenReturn(true);
        assertThat(service.userExists(testUser.getEmail())).isTrue();
    }

    @Test
    public void userNotExistsTest() {
        User testUser = new User("a@b.com", null, false, null, null);
        when(userRepo.existsUserByEmail("a@b.com")).thenReturn(false);
        assertThat(service.userExists(testUser.getEmail())).isFalse();
    }

    @Test
    public void updateGenderTest() {
        User existingUser = new User("a@b.com", "Male", false, "4+", "Laga");
        User newUser = new User("a@b.com", "Female", false, null, null);

        when(userRepo.getUserByEmail("a@b.com")).thenReturn(existingUser);

        User updatedUser = service.updateUser(newUser);

        assertThat(updatedUser.getGender()).isEqualTo("Female");
        assertThat(updatedUser.getOrganisation()).isEqualTo("Laga");


        verify(userRepo, times(1)).save(updatedUser);
    }

    @Test
    public void updateOrganisationTest() {
        User existingUser = new User("a@b.com", "Male", false, "4+", "Laga");
        User newUser = new User("a@b.com", null, false, null, "ProRow");

        when(userRepo.getUserByEmail("a@b.com")).thenReturn(existingUser);

        User updatedUser = service.updateUser(newUser);

        assertThat(updatedUser.getGender()).isEqualTo("Male");
        assertThat(updatedUser.getOrganisation()).isEqualTo("ProRow");


        verify(userRepo, times(1)).save(updatedUser);
    }

    @Test
    public void updateCertificate() {
        User existingUser = new User("a@b.com", "Male", false, "4+", "Laga");
        User newUser = new User("a@b.com", null, false, "8+", null);

        when(userRepo.getUserByEmail("a@b.com")).thenReturn(existingUser);

        User updatedUser = service.updateUser(newUser);

        assertThat(updatedUser.getCertificate()).isEqualTo("8+");
        assertThat(updatedUser.getOrganisation()).isEqualTo("Laga");


        verify(userRepo, times(1)).save(updatedUser);
    }

    @Test
    public void updateCompetitiveness() {
        User existingUser = new User("a@b.com", "Male", false, "4+", "Laga");
        User newUser = new User("a@b.com", null, true, null, null);

        when(userRepo.getUserByEmail("a@b.com")).thenReturn(existingUser);

        User updatedUser = service.updateUser(newUser);

        assertThat(updatedUser.isCompetitive()).isTrue();
        assertThat(updatedUser.getOrganisation()).isEqualTo("Laga");


        verify(userRepo, times(1)).save(updatedUser);
    }

    @Test
    public void invalidUpdate() {
        User testUser = new User("invalidEmail", null, false, null, null);
        assertThrows(IllegalArgumentException.class, () -> {
            assertThat(service.updateUser(testUser)).isNull();
        });
        verify(userRepo, never()).save(testUser);
    }
}
