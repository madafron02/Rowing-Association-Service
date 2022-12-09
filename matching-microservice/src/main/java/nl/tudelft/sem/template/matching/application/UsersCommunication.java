package nl.tudelft.sem.template.matching.application;

import jakarta.ws.rs.core.HttpHeaders;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Consumer for the Users microservice to get info from it.
 */
public class UsersCommunication {

    private static final String SERVER = "http://localhost:8082";

    /**
     * Method for calling the api request in the User microservice to get the details of an user.
     *
     * @param userId the id of the user
     * @return the details of the user
     */
    public static String getUserDetails(String userId) {
        return new ResteasyClientBuilder().build()
                .target(SERVER)
                .path("/hello")
                .request(APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer "
                        + SecurityContextHolder.getContext().getAuthentication().getCredentials())
                .accept(APPLICATION_JSON)
                .get(String.class);
    }
}
