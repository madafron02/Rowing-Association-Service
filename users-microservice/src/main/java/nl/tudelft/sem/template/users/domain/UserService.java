package nl.tudelft.sem.template.users.domain;

import nl.tudelft.sem.template.users.application.MatchingCommunication;
import nl.tudelft.sem.template.users.domain.database.OrganisationRepo;
import nl.tudelft.sem.template.users.domain.database.UserRepo;
import nl.tudelft.sem.template.users.domain.handlers.UserValidationHandler;
import nl.tudelft.sem.template.users.domain.handlers.OrganisationValidationHandler;
import nl.tudelft.sem.template.users.domain.handlers.EmailValidationHandler;
import nl.tudelft.sem.template.users.domain.handlers.CertificateValidationHandler;
import nl.tudelft.sem.template.users.domain.handlers.GenderValidationHandler;



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
    public transient UserValidationHandler userValidationHandler;



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
        userValidationHandlerSetUp();
    }

    /**
     * Sets up the chain of responsibility for validating user details that a client enters into the system.
     */
    public final void userValidationHandlerSetUp() {
        this.userValidationHandler = new EmailValidationHandler();
        UserValidationHandler genderValidationHandler = new GenderValidationHandler();
        this.userValidationHandler.setNext(genderValidationHandler);
        UserValidationHandler organisationValidationHandler = new OrganisationValidationHandler(organisationRepo);
        genderValidationHandler.setNext(organisationValidationHandler);
        UserValidationHandler certificateValidationHandler = new CertificateValidationHandler(matchingCommunication);
        organisationValidationHandler.setNext(certificateValidationHandler);
    }

    /**
     * Method for getting the User object from the repository from the user's id.
     *
     * @param email the users email
     * @return the user object
     */
    public User getByEmail(String email) {
        if (!userRepo.existsUserByEmail(email)) {
            return null;
        }
        return userRepo.getUserByEmail(email);
    }


    /**
     * Saves user in database if the user has a valid email and there isn't already a user with that email in the db
     * and the user details are valid.
     *
     * @param newUser user to save
     * @return user entity saved if successful, otherwise throws exception.
     */
    public User saveUser(User newUser) throws IllegalArgumentException {
        try {
            userValidationHandler.handle(newUser);
            userRepo.save(newUser);
            return newUser;
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Checks whether user is present in user repository by email.
     *
     * @param email email of user to check presence of
     * @return whether user is present in user repository
     */
    public boolean userExists(String email) {
        return userRepo.existsUserByEmail(email);
    }

    /**
     * Updates the user attributes that are not null in the user argument, if the new details are valid.
     * Note that when calling save with the updated entity, spring will actually do an update.
     *
     * @param newData - user with updates data
     * @return the updated user entity if successful, otherwise throws exception.
     */
    public User updateUser(User newData) {
        try {
            userValidationHandler.handle(newData);
            User existingUser = userRepo.getUserByEmail(newData.getEmail());

            existingUser.setGender(newData.getGender() == null ? existingUser.getGender() : newData.getGender());
            existingUser.setCertificate(newData.getCertificate() == null ? existingUser.getCertificate()
                    : newData.getCertificate());
            existingUser.setOrganisation(newData.getOrganisation() == null ? existingUser.getOrganisation()
                    : newData.getOrganisation());
            existingUser.setCompetitiveness(newData.isCompetitive());

            userRepo.save(existingUser);
            return existingUser;
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }
}
