package nl.tudelft.sem.template.auth.application.handlers;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import nl.tudelft.sem.template.auth.domain.AccountCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * AuthHandler that handles the creation of a JSON Web Token.
 * After the handle method has succeeded, the token can be extracted using the getToken method.
 */
public class CreateToken implements AuthHandler{

    public static final long JWT_TOKEN_VALIDITY = 30 * 60 * 1000;

    private String jwtSecret;
    private AuthHandler next;
    private ExceptionHandler exceptionHandler;
    private AccountCredentials credentials;
    private String token;

    /**
     * Constructs a CreateToken handler that handles the creation of a JWT token.
     *
     * @param jwtSecret The secret that will be used to create the JWT signature.
     */
    public CreateToken(String jwtSecret){
        this.jwtSecret = jwtSecret;
    }

    /**
     * Creates a JSON Web Token (JWT) from the userId.
     * This method will fail if no ExceptionHandler has been set.
     *
     * @param credentials The client's credentials used to create the JWT.
     */
    @Override
    public void handle(AccountCredentials credentials) {
        if(exceptionHandler == null) return;
        this.credentials = credentials;
        try{
            Map<String, Object> claims = new HashMap<>();
            long time = Instant.now().toEpochMilli();
            Date issued = new Date(time);
            Date expires = new Date(time + JWT_TOKEN_VALIDITY);
            this.token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(credentials.getUserId())
                    .setIssuedAt(issued)
                    .setExpiration(expires)
                    .signWith(SignatureAlgorithm.HS512, jwtSecret)
                    .compact();

        } catch (Exception e){
            exceptionHandler.handleException(e);
        }
    }
    
    /**
     * Sets the handler that can be called after at the end of the handle method.
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
     * Retrieves the created JSON Web Token as a String.
     *
     * @return The JSON Web Token created by the handle method. Null if handle() was not called or failed.
     */
    public String getToken(){
        return token;
    }
}
