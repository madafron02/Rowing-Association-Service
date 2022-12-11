package nl.tudelft.sem.template.users.domain;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;

@SpringBootTest
public class UserServiceTest {

    @MockBean
    private UserRepo repo;

    @Autowired
    private UserService service;

    @Test
    public void createDuplicateUserTest() {
        when(repo.existsUserByEmail("ealstad@tudelft.nl")).thenReturn(true);
        User newUser = service.createUser("ealstad@tudelft.nl");

        assertThat(newUser).isNull();
        verify(repo, times(0)).save(any());
    }

    @Test
    public void createUserTest() {
        when(repo.existsUserByEmail("ealstad@tudelft.nl")).thenReturn(false);
        User newUser = service.createUser("ealstad@tudelft.nl");

        verify(repo, times(1)).save(newUser);
    }

    @Test
    public void getByEmailTest() {
        when(repo.existsUserByEmail("ealstad@tudelft.nl")).thenReturn(true);
        when(repo.getUserByEmail("ealstad@tudelft.nl")).thenReturn(new User("ealstad@tudelft.nl"));

        assertThat(service.getByEmail("ealstad@tudelft.nl")).isEqualTo(new User("ealstad@tudelft.nl"));
    }

    @Test
    public void getByInvalidEmailTest() {
        when(repo.existsUserByEmail("ealstad@tudelft.nl")).thenReturn(false);

        assertThat(service.getByEmail("ealstad@tudelft.nl")).isNull();
    }
}
