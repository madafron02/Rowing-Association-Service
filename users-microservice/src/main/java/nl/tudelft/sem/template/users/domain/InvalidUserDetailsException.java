package nl.tudelft.sem.template.users.domain;

public class InvalidUserDetailsException extends Exception {

    static final long serialVersionUID = 2L;

    public InvalidUserDetailsException(String email) {
        super(email);
    }
}
