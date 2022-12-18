package nl.tudelft.sem.template.auth.application.handlers;

import nl.tudelft.sem.template.auth.domain.AccountCredentials;

/**
 * Interface for Handlers in the Authentication chain of responsibility.
 */
public interface AuthHandler {

    /**
     * Main method in each handler. The method performs all the tasks the handler has to deal with.
     * When the method is completed with success, it calls the handle method on the next handler with the credentials.
     * When the method cannot complete its task, it calls the ExceptionHandler and aborts.
     *
     * @param credentials The client's credentials needed for the authentication process.
     */
    public void handle(AccountCredentials credentials);

    /**
     * Sets the handler that should be called after at the end of the handle method.
     * Must be set before handle() is called in implementations that are not the last in the chain.
     *
     * @param handler The next handler in the chain.
     */
    public void setNext(AuthHandler handler);

    /**
     * Sets the ExceptionHandler that is called when the handle method encounters an error.
     * Must always be set before handle() is called.
     *
     * @param handler The ExceptionHandler used in the chain.
     */
    public void setExceptionHandler(ExceptionHandler handler);
}
