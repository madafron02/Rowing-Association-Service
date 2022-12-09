package nl.tudelft.sem.template.matching.application;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.HttpHeaders;
import nl.tudelft.sem.template.matching.domain.TimeslotApp;
import nl.tudelft.sem.template.matching.models.ActivityAvailabilityResponseModel;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Consumer for the Activity microservice to get info from it.
 */
public class ActivityCommunication {

    private static final String SERVER = "http://localhost:8084";

    /**
     * Method for calling the api request in the Activity microservice to get the available activities for an user.
     *
     * @param availability the timeslot -> availability of user
     * @return an ActivityAvailabilityResponseModel -> list of activities
     */
    public ActivityAvailabilityResponseModel getActivitiesByAvailability(TimeslotApp availability) {
        return ClientBuilder.newClient()
                .target(SERVER)
                .path("/activity/availability")
                .request(APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer "
                        + SecurityContextHolder.getContext().getAuthentication().getCredentials())
                .accept(APPLICATION_JSON)
                .post(Entity.entity(availability, APPLICATION_JSON), ActivityAvailabilityResponseModel.class);
    }
}
