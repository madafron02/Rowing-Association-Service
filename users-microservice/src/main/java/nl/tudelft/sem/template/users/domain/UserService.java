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

    /**
     * Saves user in database if the user has a valid email and there isn't already a user with that email in the db.
     *
     * @param newUser user to save
     * @return user entity saved
     * @throws EmailAlreadyInUseException if email already in use
     */
    public User saveUser(User newUser) throws EmailAlreadyInUseException {
        if (repo.existsUserByEmail(newUser.getEmail())) {
            throw new EmailAlreadyInUseException(newUser.getEmail());
        }
        repo.save(newUser);
        return newUser;
    }

    public boolean userExists(String email) {
        return repo.existsUserByEmail(email);
    }

    /**
     * Updates the user attributes that are not null in the user argument.
     * Note that when calling save with the updated entity, spring will actually do an update
     *
     * @param user - user with updates data
     * @return the updated user entity
     */
    public User updateUser(User user) {
        User existingUser = repo.getUserByEmail(user.getEmail());

        existingUser.setGender(user.getGender() == null ? existingUser.getGender() : user.getGender());
        existingUser.setCertificate(user.getCertificate() == null ? existingUser.getCertificate() : user.getCertificate());
        existingUser.setOrganization(user.getOrganization() == null ? existingUser.getOrganization()
                : user.getOrganization());
        existingUser.setCompetitive(user.isCompetitive());

        repo.save(existingUser);
        return existingUser;
    }
}
