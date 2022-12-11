package nl.tudelft.sem.template.users.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Handles the User management.
 */

@Service
public class UserService {

    private final transient UserRepo repo;

    /**
     * Default constructor.
     *
     * @param repo the user repository
     */
    @Autowired
    public UserService(UserRepo repo) {
        this.repo = repo;
    }

    /**
     * Method for getting the User object from the repository from the user's id.
     *
     * @param id the users email
     * @return the user object
     */
    public User getByEmail(String id) {
        if (!repo.existsUserByEmail(id)) {
            return null;
        }
        return repo.getUserByEmail(id);
    }

    /**
     * Method for creating a new user with given id and adding them to user repository.
     *
     * @param email id of the new user
     * @return the user entity if successfully created, null if unsuccessful
     */
    public User createUser(String email) {
        if (email == null || repo.existsUserByEmail(email)) {
            return null;
        }
        User newUser = new User(email);
        repo.save(newUser);
        return newUser;
    }
}
