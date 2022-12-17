package nl.tudelft.sem.template.auth.application.handlers;

import nl.tudelft.sem.template.auth.domain.AccountCredentials;
import nl.tudelft.sem.template.auth.domain.AccountsRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Optional;

/**
 * AuthHandler that verifies the clients credentials.
 */
public class VerifyCredentials implements AuthHandler {

    private transient AuthHandler next;
    private transient ExceptionHandler exceptionHandler;
    private transient AccountCredentials credentials;
    private transient AccountsRepo accountsRepo;

    /**
     * Constructs a VerifyCredentials handler.
     *
     * @param accountsRepo The repository the accounts are stored in.
     */
    public VerifyCredentials(AccountsRepo accountsRepo) {
        this.accountsRepo = accountsRepo;
    }

    /**
     * Retrieves known credentials from the database and matches them with the supplied credentials.
     * This method will fail if the supplied credentials do not match any credentials found in the database.
     * The method aborts if no next handler or ExceptionHandler has been set.
     *
     * @param credentials The client's credentials to be verified.
     */
    @Override
    public void handle(AccountCredentials credentials) {
        if (next == null || exceptionHandler == null) {
            return;
        }
        this.credentials = credentials;
        try {
            Optional<AccountCredentials> foundAccount = accountsRepo.findById(credentials.getUserId());
            if (foundAccount.isEmpty()) {
                exceptionHandler.handleException(new SQLException(), "UserId or password incorrect. Please try again.",
                        401);
                return;
            }
            AccountCredentials foundCredentials = foundAccount.get();
            if (!foundCredentials.getUserId().equals(credentials.getUserId())
                    || !passwordMatches(credentials.getPassword(), foundCredentials.getPassword())) {
                exceptionHandler.handleException(new SQLException(), "UserId or password incorrect. Please try again.",
                        401);
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
     * Checks if a hashed password from the database matches a real password.
     *
     * @param realPassword The original password in plain text.
     * @param encodedPassword A hashed version of the password.
     * @return True if the encoded password is a hashed version of the real password. False otherwise.
     */
    public boolean passwordMatches(String realPassword, String encodedPassword) {
        PasswordEncoder encoder = new BCryptPasswordEncoder(12, new SecureRandom());
        return encoder.matches(realPassword, encodedPassword);
    }
}
