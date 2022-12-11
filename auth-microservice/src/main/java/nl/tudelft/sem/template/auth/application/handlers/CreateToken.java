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

public class CreateToken implements AuthHandler{

    public static final long JWT_TOKEN_VALIDITY = 30 * 60 * 1000;

    private String jwtSecret;
    private AuthHandler next;
    private ExceptionHandler exceptionHandler;
    private AccountCredentials credentials;
    private String token;

    public CreateToken(String jwtSecret){
        this.jwtSecret = jwtSecret;
    }

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

    @Override
    public void setNext(AuthHandler next) {
        this.next = next;
    }

    @Override
    public void setExceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public String getToken(){
        return token;
    }
}
