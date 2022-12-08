package nl.tudelft.sem.template.matching.models;

import lombok.Data;
import nl.tudelft.sem.template.matching.domain.TimeslotApp;

/**
 * Model representing the body of the API request of Matching microservice
 * to the Notification one in the case we want to email the owner of the activity.
 */
@Data
public class NotificationRequestModelOwner {
    private String ownerId;
    private String participantId;
    private long activityId;
    private TimeslotApp timeslot;
}
