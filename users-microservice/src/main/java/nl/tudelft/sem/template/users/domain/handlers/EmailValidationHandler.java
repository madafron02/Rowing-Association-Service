package nl.tudelft.sem.template.users.domain.handlers;

import nl.tudelft.sem.template.users.domain.User;

public class EmailValidationHandler implements UserValidationHandler {

    private transient UserValidationHandler next;

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
     * verifies whether email exists and is valid.
     *
     * @param user user with email to validate
     * @return true if email is validated and this is the last handler in chain, passes to next handler if validated
     *     and this is not the last handler. Throws exception if email is not validated.
     */
    @Override
    public boolean handle(User user) {
        if (user.getEmail() != null && validateEmail(user.getEmail())) {
            if (next != null) {
                return next.handle(user);
            } else {
                return true;
            }

        }
        throw new IllegalArgumentException("This is not a valid email.");
    }

    @Override
    public UserValidationHandler getNext() {
        return next;
    }


    /**
     * Checks that an email is valid and matches the regex.
     *
     * @param email email to be checked
     * @return whether email is a valid email
     */
    public boolean validateEmail(String email) {
        return email.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\"
                + "x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")"
                + "@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0"
                + "-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z"
                + "0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b"
                + "\\x0c\\x0e-\\x7f])+)\\])");
    }
}
