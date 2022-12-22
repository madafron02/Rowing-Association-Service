package nl.tudelft.sem.template.users.domain.handlers;

import nl.tudelft.sem.template.users.application.MatchingCommunication;
import nl.tudelft.sem.template.users.domain.User;

public class CertificateValidationHandler implements UserValidationHandler {

    private transient UserValidationHandler next;
    private final transient MatchingCommunication matchingCommunication;


    public CertificateValidationHandler(MatchingCommunication matchingCommunication) {
        this.matchingCommunication = matchingCommunication;
    }


    @Override
    public void setNext(UserValidationHandler handler) {
        this.next = handler;
    }

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
}
