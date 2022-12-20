package nl.tudelft.sem.template.users.domain;

import nl.tudelft.sem.template.users.application.MatchingCommunication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Handles the User management.
 */

@Service
public class UserService {

    private final transient UserRepo repo;
    private final transient MatchingCommunication matchingCommunication;


    /**
     * Default constructor.
     *
     * @param repo the user repository
     * @param matchingCommunication communication to the matching microservice
     */
    @Autowired
    public UserService(UserRepo repo, MatchingCommunication matchingCommunication) {
        this.repo = repo;
        this.matchingCommunication = matchingCommunication;
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
     * Saves user in database if the user has a valid email and there isn't already a user with that email in the db.
     *
     * @param newUser user to save
     * @return user entity saved
     * @throws EmailAlreadyInUseException if email already in use
     * @throws InvalidUserDetailsException if user has invalid data
     */
    public User saveUser(User newUser) throws EmailAlreadyInUseException, InvalidUserDetailsException {
        if (repo.existsUserByEmail(newUser.getEmail())) {
            throw new EmailAlreadyInUseException(newUser.getEmail());
        }
        if (!newUser.validateUserInfo()) {
            throw new InvalidUserDetailsException(newUser.getEmail());
        }
        if (newUser.getCertificate() != null && !matchingCommunication.validateCertificate(newUser.getCertificate())) {
            throw new InvalidUserDetailsException(newUser.getCertificate());
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
     * @param newData - user with updates data
     * @return the updated user entity
     */
    public User updateUser(User newData) throws InvalidUserDetailsException {
        if (!newData.validateUserInfo()) {
            throw new InvalidUserDetailsException(newData.getEmail());
        }
        if (newData.getCertificate() != null && !matchingCommunication.validateCertificate(newData.getCertificate())) {
            throw new InvalidUserDetailsException(newData.getCertificate());
        }

        User existingUser = repo.getUserByEmail(newData.getEmail());

        existingUser.setGender(newData.getGender() == null ? existingUser.getGender() : newData.getGender());
        existingUser.setCertificate(newData.getCertificate() == null ? existingUser.getCertificate()
                : newData.getCertificate());
        existingUser.setOrganisation(newData.getOrganisation() == null ? existingUser.getOrganisation()
                : newData.getOrganisation());
        existingUser.setCompetitiveness(newData.isCompetitive());


        repo.save(existingUser);
        return existingUser;
    }
}
