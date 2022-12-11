package nl.tudelft.sem.template.users.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, String> {

    User getUserByEmail(String id);

    boolean existsUserByEmail(String id);
}
