package nl.tudelft.sem.template.users.domain;

public class EmailAlreadyInUseException extends Exception {

    static final long serialVersionUID = 1L;

    public EmailAlreadyInUseException(String email) {
        super(email);
    }
}

