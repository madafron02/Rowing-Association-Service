package nl.tudelft.sem.template.users.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Handles the User management.
 */

@Service
public class UserService {

    private final UserRepo repo;

    /**
     * Default constructor.
     *
     * @param repo the user repository
     */
    @Autowired
    public UserService(UserRepo repo) {
        this.repo=repo;
    }

    /**
     * Method for getting the User object from the repository from the user's id.
     * @param id the users email
     * @return the user object
     */
    public User getById(String id) {
        if (!repo.existsUserById(id)) {
            return null;
        }
        return repo.getUserById(id);
    }
}
