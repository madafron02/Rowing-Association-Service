package nl.tudelft.sem.template.users.domain.handlers;

import nl.tudelft.sem.template.users.application.MatchingCommunication;
import nl.tudelft.sem.template.users.domain.User;

public class CertificateValidationHandler implements UserValidationHandler {

    private transient UserValidationHandler next;
    private final transient MatchingCommunication matchingCommunication;

    /**
     * Constructor for CertificateValidationHandler.
     *
     * @param matchingCommunication communication class responsible for validating certificates
     */
    public CertificateValidationHandler(MatchingCommunication matchingCommunication) {
        this.matchingCommunication = matchingCommunication;
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
     * verifies whether the certificate is valid.
     *
     * @param user user with certificate to validate
     * @return true if certificate is validated and this is the last handler in chain, passes to next handler if validated
     *     and this is not the last handler. Throws exception if certificate is not validated.
     */
    @Override
    public boolean handle(User user) {
        if (user.getCertificate() == null || matchingCommunication.validateCertificate(user.getCertificate())) {
            if (next != null) {
                return next.handle(user);
            } else {
                return true;
            }

        }
        throw new IllegalArgumentException("This certificate is not recognized by our system.");
    }

    @Override
    public UserValidationHandler getNext() {
        return next;
    }
}
