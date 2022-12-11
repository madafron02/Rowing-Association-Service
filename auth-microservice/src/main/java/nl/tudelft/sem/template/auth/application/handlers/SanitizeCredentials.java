package nl.tudelft.sem.template.auth.application.handlers;

import nl.tudelft.sem.template.auth.domain.AccountCredentials;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;

public class SanitizeCredentials implements AuthHandler{

    private AuthHandler next;
    private ExceptionHandler exceptionHandler;
    private AccountCredentials credentials;

    public SanitizeCredentials(){}

    @Override
    public void handle(AccountCredentials credentials) {
        if(next == null || exceptionHandler == null) return;
        this.credentials = credentials;
        try {
            if(credentials.getUserId() == null || credentials.getUserId().equals("")){
                exceptionHandler.handleException(new IllegalArgumentException(), "Please provide a user id.");
                return;
            }
            if(credentials.getPassword() == null || credentials.getPassword().equals("")){
                exceptionHandler.handleException(new IllegalArgumentException(), "Please provide a password.");
                return;
            }
            String sanitizedUserId = sanitize(credentials.getUserId());
            String sanitizedPassword = sanitize(credentials.getPassword());
            if(sanitizedUserId.length() < credentials.getUserId().length()){
                exceptionHandler.handleException(new IllegalArgumentException(), "Your user id contains illegal" +
                        " characters. Please only use letters, numbers and the following characters: " +
                        "!#$%&()*+,-./:;<=>?@^_`{|}~");
                return;
            }
            if(sanitizedPassword.length() < credentials.getPassword().length()){
                exceptionHandler.handleException(new IllegalArgumentException(), "Your password contains illegal" +
                        " characters. Please only use letters, numbers and the following characters: " +
                        "!#$%&()*+,-./:;<=>?@^_`{|}~");
                return;
            }
            if(!isEmailAddress(sanitizedUserId)){
                exceptionHandler.handleException(new IllegalArgumentException(), "Your user id must be an email address.");
                return;
            }
            AccountCredentials newCredentials = new AccountCredentials(sanitizedUserId, hashPassword(sanitizedPassword));
            next.handle(newCredentials);
        } catch (Exception e){
            if(exceptionHandler != null){
                exceptionHandler.handleException(e);
            } else {
                throw e;
            }
        }
    }

    @Override
    public void setNext(AuthHandler handler) {
        this.next = handler;
    }

    @Override
    public void setExceptionHandler(ExceptionHandler handler) {
        this.exceptionHandler = handler;
    }


    private String sanitize(String s){
        String res =  s.replaceAll("[^a-zA-Z0-9!#$%&()*+,-./:;<=>?@^_`{|}~]+", "");
        res = res.replaceAll("null", "").replaceAll("NULL", "");
        return res;
    }

    private boolean isEmailAddress(String s){
        return s.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\" +
                "x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")" +
                "@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0" +
                "-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z" +
                "0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b" +
                "\\x0c\\x0e-\\x7f])+)\\])");
    }

    private String hashPassword(String password){
        PasswordEncoder encoder = new BCryptPasswordEncoder(12, new SecureRandom());
        return encoder.encode(password);
    }
}
