package nl.tudelft.sem.template.users.domain;


import nl.tudelft.sem.template.users.domain.database.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@SpringBootTest
public class UserServiceTest {

    @MockBean
    private UserRepo repo;

    @Autowired
    private UserService service;

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
