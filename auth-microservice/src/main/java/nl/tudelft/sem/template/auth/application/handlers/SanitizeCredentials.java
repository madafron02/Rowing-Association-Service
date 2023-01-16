package nl.tudelft.sem.template.auth.application.handlers;

import nl.tudelft.sem.template.auth.domain.AccountCredentials;

/**
 * AuthHandler that handles the sanitizes the clients credentials and hashes the password.
 */
public class SanitizeCredentials implements AuthHandler {

    private transient AuthHandler next;
    private transient ExceptionHandler exceptionHandler;
    private transient AccountCredentials credentials;
    private transient String sanitizedUserId;
    private transient String sanitizedPassword;

    /**
     * Constructs a SanitizeCredentials handler.
     */
    public SanitizeCredentials() {}

    /**
     * Checks if the credentials are complete and don't contain any illegal characters.
     * This method will fail if the credentials are incomplete or contain illegal characters,
     * or if the userId is not an email-address.
     * The method will abort if the next handler or ExceptionHandler are not set.
     *
     * @param credentials The client's credentials that will be sanitized.
     */
    @Override
    public void handle(AccountCredentials credentials) {
        if (next == null || exceptionHandler == null) {
            return;
        }
        this.credentials = credentials;
        if (credentialsNotEmpty()) {
            sanitizedUserId = sanitize(credentials.getUserId());
            sanitizedPassword = sanitize(credentials.getPassword());
            if (credentialsClean()) {
                AccountCredentials newCredentials = new AccountCredentials(sanitizedUserId, sanitizedPassword);
                next.handle(newCredentials);
            }
        }
    }

    /**
     * Sets the handler that should be called after at the end of the handle method.
     * Must be set before handle() is called.
     *
     * @param handler The next handler in the chain.
     */
    @Override
    public void setNext(AuthHandler handler) {
        this.next = handler;
    }

    /**
     * Sets the ExceptionHandler that is called when the handle method encounters an error.
     * Must always be set before handle() is called.
     *
     * @param handler The ExceptionHandler used in the chain.
     */
    @Override
    public void setExceptionHandler(ExceptionHandler handler) {
        this.exceptionHandler = handler;
    }

    /**
     * Removes any non permitted characters from a String.
     *
     * @param s The String to be sanitized.
     * @return The sanitized String.
     */
    private String sanitize(String s) {
        String res =  s.replaceAll("[^a-zA-Z0-9!#$%&()*+,-./:;<=>?@^_`{|}~]+", "");
        res = res.replaceAll("null", "").replaceAll("NULL", "");
        return res;
    }

    /**
     * Checks if the required credentials are present.
     * Sets the correct exception and status if this is not the case.
     *
     * @return False if one of the credentials is empty. True otherwise.
     */
    private boolean credentialsNotEmpty() {
        if (credentials.getUserId() == null || credentials.getUserId().equals("")) {
            exceptionHandler.handleException(new IllegalArgumentException(), "Please provide a user id.", 400);
            return false;
        }
        if (credentials.getPassword() == null || credentials.getPassword().equals("")) {
            exceptionHandler.handleException(new IllegalArgumentException(), "Please provide a password.", 400);
            return false;
        }
        return true;
    }

    /**
     * Checks if the original credentials were clean and correctly formatted by comparing them to their sanitized version.
     * Sets the correct exception and status if this is not the case.
     *
     * @return True if the credentials were clean. False otherwise.
     */
    private boolean credentialsClean() {
        if (sanitizedUserId.length() < credentials.getUserId().length()) {
            exceptionHandler.handleException(new IllegalArgumentException(), "Your user id contains illegal"
                    + " characters. Please only use letters, numbers and the following characters: "
                    + "!#$%&()*+,-./:;<=>?@^_`{|}~", 400);
            return false;
        }
        if (sanitizedPassword.length() < credentials.getPassword().length()) {
            exceptionHandler.handleException(new IllegalArgumentException(), "Your password contains illegal"
                    + " characters. Please only use letters, numbers and the following characters: "
                    + "!#$%&()*+,-./:;<=>?@^_`{|}~", 400);
            return false;
        }
        if (!isEmailAddress(sanitizedUserId)) {
            exceptionHandler.handleException(new IllegalArgumentException(), "Your user id must be an email address.",
                    400);
            return false;
        }
        return true;
    }


    /**
     * Checks if a String has the format of an email-address.
     *
     * @param s The String to be checked.
     * @return True if the String is an email-address. False otherwise.
     */
    private boolean isEmailAddress(String s) {
        return s.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\"
                + "x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")"
                + "@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0"
                + "-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z"
                + "0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b"
                + "\\x0c\\x0e-\\x7f])+)\\])");
    }


}
