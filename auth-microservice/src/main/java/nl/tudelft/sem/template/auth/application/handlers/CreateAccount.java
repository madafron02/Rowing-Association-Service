package nl.tudelft.sem.template.auth.application.handlers;

import nl.tudelft.sem.template.auth.domain.AccountCredentials;
import nl.tudelft.sem.template.auth.domain.AccountsRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Optional;

/**
 * AuthHandler that handles the creation of new accounts.
 */
public class CreateAccount implements AuthHandler {

    private transient AuthHandler next;
    private transient ExceptionHandler exceptionHandler;
    private transient AccountCredentials credentials;
    private transient AccountsRepo accountsRepo;

    /**
     * Constructs a CreateAccount handler.
     * Handles the creation of new accounts.
     *
     * @param accountsRepo The repository where the accounts are saved.
     */
    public CreateAccount(AccountsRepo accountsRepo) {
        this.accountsRepo = accountsRepo;
    }

    /**
     * Creates an account and saves it in the accounts' repository.
     * This method will fail if an account with the supplied userId already exists.
     * The method will abort if the next handler or ExceptionHandler are not set.
     *
     * @param credentials The client's credentials (with a hashed password) to be saved in the database.
     */
    @Override
    public void handle(AccountCredentials credentials) {
        if (next == null || exceptionHandler == null) {
            return;
        }
        this.credentials = credentials;
        try {
            if (accountExists()) {
                exceptionHandler.handleException(new SQLException(), "An account with this user id already exists."
                        + " Please choose a different user id.", 400);
                return;
            }
            String hashedPassword = hashPassword(credentials.getPassword());
            AccountCredentials hashedCredentials = new AccountCredentials(credentials.getUserId(), hashedPassword);
            accountsRepo.save(hashedCredentials);
            if (!verifySavedAccount(credentials.getPassword())) {
                exceptionHandler.handleException(new SQLException(), "There was an error while saving your account."
                        + " Please try again later", 500);
                return;
            }
            next.handle(credentials);
        } catch (Exception e) {
            exceptionHandler.handleException(e);
        }
    }

    /**
     * Sets the handler that should be called after at the end of the handle method.
     * Must be set before handle() is called.
     *
     * @param next The next handler in the chain.
     */
    @Override
    public void setNext(AuthHandler next) {
        this.next = next;
    }

    /**
     * Sets the ExceptionHandler that is called when the handle method encounters an error.
     * Must always be set before handle() is called.
     *
     * @param exceptionHandler The ExceptionHandler used in the chain.
     */
    @Override
    public void setExceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    /**
     * Checks if an account with the supplied userId already exists in the database.
     *
     * @return True if an account with the userId already exists. False otherwise.
     */
    private boolean accountExists() {
        Optional<AccountCredentials> foundAccount = accountsRepo.findById(credentials.getUserId());
        return foundAccount.isPresent();
    }

    /**
     * Checks that the userId and password were saved correctly.
     *
     * @return True if an entry with the correct credentials exits. False otherwise.
     */
    private boolean verifySavedAccount(String password) {
        Optional<AccountCredentials> foundAccount = accountsRepo.findById(credentials.getUserId());
        if (!foundAccount.isPresent()) {
            return false;
        }
        PasswordEncoder encoder = new BCryptPasswordEncoder(12, new SecureRandom());
        return encoder.matches(password, foundAccount.get().getPassword());
    }

    /**
     * Creates a hash from a password.
     *
     * @param password The password to be hashed.
     * @return A String representing the hash of the password.
     */
    private String hashPassword(String password) {
        PasswordEncoder encoder = new BCryptPasswordEncoder(12, new SecureRandom());
        return encoder.encode(password);
    }


}
