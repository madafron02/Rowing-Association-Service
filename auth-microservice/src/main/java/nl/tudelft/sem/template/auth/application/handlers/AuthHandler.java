package nl.tudelft.sem.template.auth.application.handlers;

import nl.tudelft.sem.template.auth.domain.AccountCredentials;

public interface AuthHandler {

    public void handle(AccountCredentials credentials);

    public void setNext(AuthHandler handler);

    public void setExceptionHandler(AuthHandler handler);
}
