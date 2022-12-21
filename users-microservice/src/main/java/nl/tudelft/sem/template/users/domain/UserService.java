package nl.tudelft.sem.template.users.domain;

import nl.tudelft.sem.template.users.application.MatchingCommunication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Handles the User management.
 */

@Service
public class UserService {

    private final transient UserRepo userRepo;
    private final transient OrganisationRepo organisationRepo;
    private final transient MatchingCommunication matchingCommunication;


    /**
     * Default constructor.
     *
     * @param userRepo the user repository
     * @param organisationRepo the organisation name repository
     * @param matchingCommunication communication to the matching microservice
     */
    @Autowired
    public UserService(UserRepo userRepo, OrganisationRepo organisationRepo, MatchingCommunication matchingCommunication) {
        this.userRepo = userRepo;
        this.organisationRepo = organisationRepo;
        this.matchingCommunication = matchingCommunication;
    }

    /**
     * Method for getting the User object from the repository from the user's id.
     *
     * @param id the users email
     * @return the user object
     */
    public User getByEmail(String id) {
        if (!userRepo.existsUserByEmail(id)) {
            return null;
        }
        return userRepo.getUserByEmail(id);
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
        if (userRepo.existsUserByEmail(newUser.getEmail())) {
            throw new EmailAlreadyInUseException(newUser.getEmail());
        }
        if (!newUser.validateUserInfo()) {
            throw new InvalidUserDetailsException(newUser.getEmail());
        }
        if (newUser.getCertificate() != null && !matchingCommunication.validateCertificate(newUser.getCertificate())) {
            throw new InvalidUserDetailsException(newUser.getCertificate());
        }
        if (newUser.getOrganisation() != null && !validateOrganisation(newUser.getOrganisation())) {
            throw new InvalidUserDetailsException(newUser.getOrganisation());
        }
        userRepo.save(newUser);
        return newUser;
    }

    public boolean userExists(String email) {
        return userRepo.existsUserByEmail(email);
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
        if (newData.getOrganisation() != null && !validateOrganisation(newData.getOrganisation())) {
            throw new InvalidUserDetailsException(newData.getOrganisation());
        }

        User existingUser = userRepo.getUserByEmail(newData.getEmail());

        existingUser.setGender(newData.getGender() == null ? existingUser.getGender() : newData.getGender());
        existingUser.setCertificate(newData.getCertificate() == null ? existingUser.getCertificate()
                : newData.getCertificate());
        existingUser.setOrganisation(newData.getOrganisation() == null ? existingUser.getOrganisation()
                : newData.getOrganisation());
        existingUser.setCompetitiveness(newData.isCompetitive());

        userRepo.save(existingUser);
        return existingUser;
    }

    public boolean validateOrganisation(String organisationName) {
        return organisationRepo.existsOrganisationByName(organisationName);
    }

    public void addOrganisation(Organisation newOrganisation) {
        organisationRepo.save(newOrganisation);
    }
}
