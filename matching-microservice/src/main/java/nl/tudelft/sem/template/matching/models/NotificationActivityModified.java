package nl.tudelft.sem.template.matching.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.matching.domain.TimeslotApp;

/**
 * Model representing the information needed for the Notification subsystem to email
 * the participant about the change of activity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationActivityModified {
    private String participantId;
    private long activityId;
    private TimeslotApp timeslot;
}
