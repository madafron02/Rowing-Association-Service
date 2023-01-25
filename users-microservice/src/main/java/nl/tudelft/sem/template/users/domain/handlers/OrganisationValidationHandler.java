package nl.tudelft.sem.template.users.domain.handlers;

import nl.tudelft.sem.template.users.domain.User;
import nl.tudelft.sem.template.users.domain.database.OrganisationRepo;


public class OrganisationValidationHandler implements UserValidationHandler {

    private transient UserValidationHandler next;
    private final transient OrganisationRepo organisationRepo;

    /**
     * Constructor for OrganisationValidationHandler.
     *
     * @param organisationRepo repository responsible for storing recognised organisations
     */
    public OrganisationValidationHandler(OrganisationRepo organisationRepo) {
        this.organisationRepo = organisationRepo;
    }

    /**
     * Sets next handler in chain of responsibility.
     *
     * @param handler next handler in chain
     */
    @Override
    public void setNext(UserValidationHandler handler) {
        this.next = handler;
    }

    /**
     * verifies whether organisation is recognised by system.
     *
     * @param user user with organisation to validate
     * @return true if organisation is validated and this is the last handler in chain, passes to next handler if validated
     *     and this is not the last handler. Throws exception if organisation is not validated.
     */
    @Override
    public boolean handle(User user) {
        if (user.getOrganisation() == null || organisationRepo.existsOrganisationByName(user.getOrganisation())) {
            if (next != null) {
                return next.handle(user);
            } else {
                return true;
            }

        }
        throw new IllegalArgumentException("This organisation is not recognised by our system.");
    }

    @Override
    public UserValidationHandler getNext() {
        return next;
    }
}
