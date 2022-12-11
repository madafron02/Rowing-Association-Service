package nl.tudelft.sem.template.auth.controllers;

import nl.tudelft.sem.template.auth.application.handlers.CreateAccount;
import nl.tudelft.sem.template.auth.application.handlers.CreateToken;
import nl.tudelft.sem.template.auth.application.handlers.ExceptionHandler;
import nl.tudelft.sem.template.auth.application.handlers.SanitizeCredentials;
import nl.tudelft.sem.template.auth.domain.AccountCredentials;
import nl.tudelft.sem.template.auth.domain.AccountsRepo;
import nl.tudelft.sem.template.auth.models.RegistrationRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Value("${jwt.secret}")
    private String jwtSecret;
    private final AccountsRepo accountsRepo;

    @Autowired
    public AuthenticationController(AccountsRepo accountsRepo) {
        this.accountsRepo = accountsRepo;
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegistrationRequestModel request) throws Exception {

        AccountCredentials credentials = new AccountCredentials(request.getUserId(), request.getPassword());

        ExceptionHandler exceptionHandler = new ExceptionHandler();
        SanitizeCredentials sanitizeCredentials = new SanitizeCredentials();
        CreateAccount createAccount = new CreateAccount(accountsRepo);
        CreateToken createToken = new CreateToken(jwtSecret);

        sanitizeCredentials.setExceptionHandler(exceptionHandler);
        createAccount.setExceptionHandler(exceptionHandler);
        createToken.setExceptionHandler(exceptionHandler);

        sanitizeCredentials.setNext(createAccount);
        createAccount.setNext(createToken);

        sanitizeCredentials.handle(credentials);

        String token = createToken.getToken();
        if(exceptionHandler.didCatchException() || token == null){
            return ResponseEntity.status(exceptionHandler.getStatusCode()).body(exceptionHandler.getErrorMessage());
        }
        return ResponseEntity.ok(token);
    }

}
