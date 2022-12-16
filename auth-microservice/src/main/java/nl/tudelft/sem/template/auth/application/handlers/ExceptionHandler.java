package nl.tudelft.sem.template.auth.application.handlers;

/**
 * Handler that handles and stores exceptions that occur in other handlers.
 */
public class ExceptionHandler {

    private Exception exception;
    private String errorMessage;
    private boolean caughtException;

    private int statusCode;

    /**
     * Constructs an ExceptionHandler.
     * Sets caughtException, errorMessage and statusCode to a default value.
     */
    public ExceptionHandler(){
        this.caughtException = false;
        this.errorMessage = "An unexpected error has occurred. Please try again.";
        this.statusCode = 500;
    }

    /**
     * Handles an exception by storing it, so it can be accessed later.
     *
     * @param exception The exception that was thrown.
     */
    public void handleException(Exception exception){
        this.caughtException = true;
        this.exception = exception;
    }

    /**
     * Handles an exception by storing it, and storing a custom error message and http status code.
     *
     * @param exception The exception that was thrown.
     * @param errorMessage A custom error message for the client.
     * @param statusCode The http status code to be used in the response.
     */
    public void handleException(Exception exception, String errorMessage, int statusCode){
        this.caughtException = true;
        this.exception = exception;
        this.errorMessage = errorMessage;
        this.statusCode = statusCode;
    }

    /**
     * Gets the exception that was handled.
     *
     * @return The handled exception. Null if no exception was handled.
     */
    public Exception getException() {
        return exception;
    }

    /**
     * Gets the custom error message.
     * Will be a default message if no specific message was registered.
     *
     * @return The custom error message.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Says if an exception was handled or not.
     *
     * @return True if an exception was handled. False otherwise.
     */
    public boolean didCatchException(){
        return caughtException;
    }

    /**
     * Gets the http status code to be used in the response.
     *
     * @return The http status code associated with the exception.
     */
    public int getStatusCode(){
        return statusCode;
    }
}
