package nl.tudelft.sem.template.auth.application.handlers;

import nl.tudelft.sem.template.auth.domain.AccountCredentials;
import nl.tudelft.sem.template.auth.domain.AccountsRepo;

import java.sql.SQLException;
import java.util.Optional;

public class CreateAccount implements AuthHandler{

    private AuthHandler next;
    private ExceptionHandler exceptionHandler;
    private AccountCredentials credentials;
    private AccountsRepo accountsRepo;

    public CreateAccount(AccountsRepo accountsRepo){
        this.accountsRepo = accountsRepo;
    }

    @Override
    public void handle(AccountCredentials credentials) {
        if(next == null || exceptionHandler == null) return;
        this.credentials = credentials;
        try {
            if(accountExists()){
                exceptionHandler.handleException(new SQLException(), "An account with this user id already exists." +
                        " Please choose a different user id.");
                return;
            }
            accountsRepo.save(credentials);
            if(!verifySavedAccount()){
                exceptionHandler.handleException(new SQLException(), "There was an error while saving your account." +
                        " Please try again later");
                return;
            }
            next.handle(credentials);
        } catch (Exception e){
            exceptionHandler.handleException(e);
        }
    }

    @Override
    public void setNext(AuthHandler next) {
        this.next = next;
    }

    @Override
    public void setExceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    private boolean accountExists(){
        Optional<AccountCredentials> foundAccount = accountsRepo.findById(credentials.getUserId());
        return foundAccount.isPresent();
    }

    private boolean verifySavedAccount(){
        Optional<AccountCredentials> foundAccount = accountsRepo.findById(credentials.getUserId());
        if(!foundAccount.isPresent()){
            return false;
        }
        return foundAccount.get().equals(credentials);
    }


}
