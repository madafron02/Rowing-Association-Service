package nl.tudelft.sem.template.activities.domain;

import lombok.NoArgsConstructor;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Communicates with the matching microservice using HTTP requests.
 */
@Component
@NoArgsConstructor
public class MatchingClient {

    private static final String SERVER = "http://localhost:8083/matching";

    /**
     * Informs the matching service to delete all matches for this Activity.
     *
     * @param activityId the id of the Activity to be deleted
     */
    public void deleteAllMatches(Long activityId) {
        new ResteasyClientBuilder().build()
                .target(SERVER)
                .path("/activity/modified")
                .request(APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer "
                        + SecurityContextHolder.getContext().getAuthentication().getCredentials())
                .accept(APPLICATION_JSON)
                .post(Entity.entity(activityId, APPLICATION_JSON));
    }

    /**
     * Calls the matching API endpoint for checking if a certificate is valid.
     *
     * @param certificate the certificate to check
     * @return a Boolean representing the result of the evaluation
     */
    public Boolean validateCertificate(String certificate) {
        return new ResteasyClientBuilder().build()
                .target(SERVER)
                .path("certificate/validate")
                .request(APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer "
                        + SecurityContextHolder.getContext().getAuthentication().getCredentials())
                .accept(APPLICATION_JSON)
                .post(Entity.entity(certificate, APPLICATION_JSON), Boolean.class);
    }
}
