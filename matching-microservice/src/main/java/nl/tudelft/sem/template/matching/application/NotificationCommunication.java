package nl.tudelft.sem.template.matching.application;

import jakarta.ws.rs.core.HttpHeaders;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.matching.models.NotificationActivityModified;
import nl.tudelft.sem.template.matching.models.NotificationRequestModelOwner;
import nl.tudelft.sem.template.matching.models.NotificationRequestModelParticipant;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


import javax.ws.rs.client.Entity;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Consumer for the Notification microservice to send info to it.
 */
@Component
@NoArgsConstructor
public class NotificationCommunication {

    private static final String SERVER = "http://localhost:8086/";

    /**
     * Method for calling an api request in the Notification microservice to send a notification.
     *
     * @param request DTO containing ownerId, participantId, activityId, and timeslot of the activity
     * @return String containing information whether the notification was successful.
     */
    public String sendReminderToOwner(NotificationRequestModelOwner request) {
        return new ResteasyClientBuilder().build()
                .target(SERVER).path("notification/publisher")
                .request(APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer "
                        + SecurityContextHolder.getContext().getAuthentication().getCredentials())
                .accept(APPLICATION_JSON)
                .post(Entity.entity(request, APPLICATION_JSON), String.class);
    }

    /**
     * Method for calling an api request in the Notification microservice to send a notification.
     *
     * @param request DTO containing participantId, activityId, timeslot of the activity, and the decision
     * @return String containing information whether the notification was successful.
     */
    public String sendNotificationToParticipant(NotificationRequestModelParticipant request) {
        return new ResteasyClientBuilder().build()
                .target(SERVER).path("notification/participant")
                .request(APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer "
                        + SecurityContextHolder.getContext().getAuthentication().getCredentials())
                .accept(APPLICATION_JSON)
                .post(Entity.entity(request, APPLICATION_JSON), String.class);
    }

    /**
     * Method for calling an API request to the Notification microservice to send a notification to users
     * about the match being discard due to an activity change.
     *
     * @param request - DTO containing participantId and activityId
     * @return String containing information whether the notification was successful
     */
    public String activityModifiedNotification(NotificationActivityModified request) {
        return new ResteasyClientBuilder().build()
                .target(SERVER).path("notification/activity-changed")
                .request(APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer "
                        + SecurityContextHolder.getContext().getAuthentication().getCredentials())
                .accept(APPLICATION_JSON)
                .post(Entity.entity(request, APPLICATION_JSON), String.class);
    }
}
