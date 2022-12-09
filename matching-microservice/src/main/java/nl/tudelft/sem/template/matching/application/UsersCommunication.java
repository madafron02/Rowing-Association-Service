package nl.tudelft.sem.template.matching.application;

import jakarta.ws.rs.core.HttpHeaders;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.matching.domain.UserApp;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


import javax.ws.rs.client.Entity;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Consumer for the Users microservice to get info from it.
 */
@Component
@NoArgsConstructor
public class UsersCommunication {

    private static final String SERVER = "http://localhost:8082";

    /**
     * Method for calling the api request in the User microservice to get the details of an user.
     *
     * @param userId the id of the user
     * @return the details of the user
     */
    public UserApp getUserDetails(String userId) {
        return new ResteasyClientBuilder().build()
                .target(SERVER)
                .path("/user/details")
                .request(APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer "
                        + SecurityContextHolder.getContext().getAuthentication().getCredentials())
                .accept(APPLICATION_JSON)
                .post(Entity.entity(userId, APPLICATION_JSON), UserApp.class);
    }
}
