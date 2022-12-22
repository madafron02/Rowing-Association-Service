package nl.tudelft.sem.template.matching.application;

import jakarta.ws.rs.core.HttpHeaders;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.matching.domain.TimeslotApp;
import nl.tudelft.sem.template.matching.models.ActivityAvailabilityResponseModel;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.Entity;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Consumer for the Activity microservice to get info from it.
 */
@Component
@NoArgsConstructor
public class ActivityCommunication {

    private static final String SERVER = "http://localhost:8084";

    /**
     * Method for calling the api request in the Activity microservice to get the available activities for an user.
     *
     * @param availability the timeslot -> availability of user
     * @return an ActivityAvailabilityResponseModel -> list of activities
     */
    public ActivityAvailabilityResponseModel getActivitiesByAvailability(TimeslotApp availability) {
        return new ResteasyClientBuilder().build()
                .target(SERVER)
                .path("/activities/within-timeslot")
                .request(APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer "
                        + SecurityContextHolder.getContext().getAuthentication().getCredentials())
                .accept(APPLICATION_JSON)
                .post(Entity.entity(availability, APPLICATION_JSON), ActivityAvailabilityResponseModel.class);
    }


    /**
     * Method for calling the api request in Activity microservice to get the timeslot of a given activity
     * by providing its id.
     *
     * @param activityId the id of the activity
     * @return the timeslot of the activity
     */
    public TimeslotApp getActivityTimeslotById(long activityId) {
        return new ResteasyClientBuilder().build()
                .target(SERVER)
                .path("/activities/" + activityId)
                .request(APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer "
                        + SecurityContextHolder.getContext().getAuthentication().getCredentials())
                .accept(APPLICATION_JSON)
                .get(TimeslotApp.class);
    }

    /**
     * Method for calling the api request in Activity microservice to decrease one of the positions
     * available for the specified activity.
     *
     * @param activityId the id of the activity
     * @param position position of the match
     */
    public void updateActivity(long activityId, String position) {
        new ResteasyClientBuilder().build()
                .target(SERVER)
                .path("/activities/update/" + activityId)
                .request(APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer "
                        + SecurityContextHolder.getContext().getAuthentication().getCredentials())
                .accept(APPLICATION_JSON)
                .put(Entity.entity(position, APPLICATION_JSON));
    }
}
