package nl.tudelft.sem.template.users.domain.handlers;

import nl.tudelft.sem.template.users.domain.User;
import nl.tudelft.sem.template.users.domain.database.OrganisationRepo;


public class OrganisationValidationHandler implements UserValidationHandler {

    private transient UserValidationHandler next;
    private final transient OrganisationRepo organisationRepo;

    public OrganisationValidationHandler(OrganisationRepo organisationRepo) {
        this.organisationRepo = organisationRepo;
    }

    @Override
    public void setNext(UserValidationHandler handler) {
        this.next = handler;
    }

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
}
