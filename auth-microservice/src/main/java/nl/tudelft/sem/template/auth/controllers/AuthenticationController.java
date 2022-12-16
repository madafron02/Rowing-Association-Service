package nl.tudelft.sem.template.auth.controllers;

import nl.tudelft.sem.template.auth.application.handlers.CreateToken;
import nl.tudelft.sem.template.auth.application.handlers.ExceptionHandler;
import nl.tudelft.sem.template.auth.domain.AccountCredentials;
import nl.tudelft.sem.template.auth.domain.AccountsRepo;
import nl.tudelft.sem.template.auth.domain.ChainCreator;
import nl.tudelft.sem.template.auth.models.RegistrationRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller that handles incoming authentication and register requests.
 */
@RestController
public class AuthenticationController {

    /**
     * The secret from the application.properties that will be used to sign the JWTs.
     */
    @Value("${jwt.secret}")
    private transient String jwtSecret;
    private final transient AccountsRepo accountsRepo;

    /**
     * Automatically constructs a Controller.
     *
     * @param accountsRepo The repository that holds the accounts.
     */
    @Autowired
    public AuthenticationController(AccountsRepo accountsRepo) {
        this.accountsRepo = accountsRepo;
    }

    /**
     * Mapping that processes an incoming request for registration.
     *
     * @param request The request model with the credentials provided by the client.
     * @return ResponseEntity with either a JWT or an error.
     * @throws Exception Throws exception when one occurs.
     * {@code @PostMapping}   /register
     */
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegistrationRequestModel request) throws Exception {
        AccountCredentials credentials = new AccountCredentials(request.getUserId(), request.getPassword());

        ExceptionHandler exceptionHandler = new ExceptionHandler();
        CreateToken createToken = ChainCreator.createRegistrationChain(exceptionHandler, accountsRepo, jwtSecret,
                credentials);

        String token = createToken.getToken();
        if (exceptionHandler.didCatchException() || token == null) {
            return ResponseEntity.status(exceptionHandler.getStatusCode()).body(exceptionHandler.getErrorMessage());
        }
        return ResponseEntity.ok(token);
    }
}
