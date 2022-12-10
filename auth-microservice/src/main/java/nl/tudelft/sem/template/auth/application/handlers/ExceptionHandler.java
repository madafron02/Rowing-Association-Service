package nl.tudelft.sem.template.auth.application.handlers;


import lombok.SneakyThrows;

public class ExceptionHandler {

    private Exception exception;
    private String errorMessage;

    public ExceptionHandler(){
        this.errorMessage = "An unexpected error has occurred. Please try again.";
    }

    @SneakyThrows
    public void throwException(Exception exception, String errorMessage){
        this.exception = exception;
        this.errorMessage = errorMessage;
        throw exception;
    }

    public Exception getException() {
        return exception;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
