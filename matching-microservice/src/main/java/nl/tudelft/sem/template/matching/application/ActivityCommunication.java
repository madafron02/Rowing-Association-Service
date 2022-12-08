package nl.tudelft.sem.template.matching.application;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import nl.tudelft.sem.template.matching.domain.ActivityApp;
import nl.tudelft.sem.template.matching.domain.TimeslotApp;
import nl.tudelft.sem.template.matching.models.ActivityAvailabilityResponseModel;

import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ActivityCommunication {

    private static final String SERVER = "http://localhost:8084";

    public ActivityAvailabilityResponseModel getActivitiesByAvailability(TimeslotApp availability) {
        return ClientBuilder.newClient()
                .target(SERVER)
                .path("/activity/availability")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(availability, APPLICATION_JSON), ActivityAvailabilityResponseModel.class);
    }
}
