package nl.tudelft.sem.template.users.application;

import jakarta.ws.rs.core.HttpHeaders;
import lombok.NoArgsConstructor;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.Entity;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Consumer to get data from matching microservice.
 */
@Component
@NoArgsConstructor
public class MatchingCommunication {

    private static final String SERVER = "http://localhost:8083";

    /**
     * Method for calling the validation method in the matching microservice for checking validity of certificate.
     *
     * @param certificate the certificate to be validated
     * @return whether the certificate is valid
     */
    public boolean validateCertificate(String certificate) {
        return new ResteasyClientBuilder().build()
                .target(SERVER)
                .path("/matching/certificate/validate")
                .request(APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer "
                        + SecurityContextHolder.getContext().getAuthentication().getCredentials())
                .accept(APPLICATION_JSON)
                .post(Entity.entity(certificate, APPLICATION_JSON), Boolean.class);
    }
}