package nl.tudelft.sem.template.auth.domain;


import nl.tudelft.sem.template.auth.application.handlers.CreateAccount;
import nl.tudelft.sem.template.auth.application.handlers.CreateToken;
import nl.tudelft.sem.template.auth.application.handlers.ExceptionHandler;
import nl.tudelft.sem.template.auth.application.handlers.SanitizeCredentials;
import nl.tudelft.sem.template.auth.application.handlers.VerifyCredentials;

/**
 * Helper class for encapsulating the creation of handler chains.
 */
public class ChainCreator {

    /**
     * Creates and starts a chain of handlers for registering new accounts.
     *
     * @param exceptionHandler The ExceptionHandler user by all handlers in the chain.
     * @param accountsRepo The repository where the accounts are stored.
     * @param jwtSecret The secret String used to sign a JWT.
     * @param credentials The client's credentials to register.
     * @return The last handler in the chain.
     */
    public static CreateToken createRegistrationChain(ExceptionHandler exceptionHandler, AccountsRepo accountsRepo,
                                                      String jwtSecret, AccountCredentials credentials) {
        SanitizeCredentials sanitizeCredentials = new SanitizeCredentials();
        CreateAccount createAccount = new CreateAccount(accountsRepo);
        CreateToken createToken = new CreateToken(jwtSecret);

        sanitizeCredentials.setExceptionHandler(exceptionHandler);
        createAccount.setExceptionHandler(exceptionHandler);
        createToken.setExceptionHandler(exceptionHandler);

        sanitizeCredentials.setNext(createAccount);
        createAccount.setNext(createToken);

        sanitizeCredentials.handle(credentials);
        return createToken;
    }

    /**
     * Creates and starts a chain of handlers for authenticating new accounts.
     *
     * @param exceptionHandler The ExceptionHandler user by all handlers in the chain.
     * @param accountsRepo The repository where the accounts are stored.
     * @param jwtSecret The secret String used to sign a JWT.
     * @param credentials The credentials the client provided.
     * @return The last handler in the chain.
     */
    public static CreateToken createAuthenticationChain(ExceptionHandler exceptionHandler, AccountsRepo accountsRepo,
                                                        String jwtSecret, AccountCredentials credentials) {
        SanitizeCredentials sanitizeCredentials = new SanitizeCredentials();
        VerifyCredentials verifyCredentials = new VerifyCredentials(accountsRepo);
        CreateToken createToken = new CreateToken(jwtSecret);

        sanitizeCredentials.setExceptionHandler(exceptionHandler);
        verifyCredentials.setExceptionHandler(exceptionHandler);
        createToken.setExceptionHandler(exceptionHandler);

        sanitizeCredentials.setNext(verifyCredentials);
        verifyCredentials.setNext(createToken);

        sanitizeCredentials.handle(credentials);
        return createToken;
    }
}
