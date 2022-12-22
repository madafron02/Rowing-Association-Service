package nl.tudelft.sem.template.matching.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.matching.domain.TimeslotApp;

/**
 * Model representing the body of the API request of Matching microservice
 * to the Notification one in the case we want to email the owner of the activity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestModelOwner {
    private String ownerId;
    private String participantId;
    private long activityId;
    private TimeslotApp timeslot;
}
