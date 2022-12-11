package nl.tudelft.sem.template.users.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepoTest {

    @Autowired
    private UserRepo repo;

    @Test
    public void getUserByTest() {
        User newUser = new User("ldicaprio@tudelft.nl");
        repo.save(newUser);
        assertThat(repo.getUserByEmail("ldicaprio@tudelft.nl")).isEqualTo(newUser);
    }

    @Test
    public void noDuplicateUsersTest() {
        User newUser = new User("ldicaprio@tudelft.nl", "male", true, "+4", "laga");
        User otherUser = new User("ldicaprio@tudelft.nl", "female", true, "+4", "laga");
        repo.save(newUser);
        repo.save(otherUser);

        assertThat(repo.count()).isEqualTo(1);
    }
}
