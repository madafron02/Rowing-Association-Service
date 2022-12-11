package nl.tudelft.sem.template.auth.application.handlers;

public class ExceptionHandler {

    private Exception exception;
    private String errorMessage;
    private boolean caughtException;

    public ExceptionHandler(){
        this.caughtException = false;
        this.errorMessage = "An unexpected error has occurred. Please try again.";
    }

    public void handleException(Exception exception){
        this.caughtException = true;
        this.exception = exception;
    }

    public void handleException(Exception exception, String errorMessage){
        this.caughtException = true;
        this.exception = exception;
        this.errorMessage = errorMessage;
    }

    public Exception getException() {
        return exception;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean didCatchException(){
        return caughtException;
    }
}
