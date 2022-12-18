package nl.tudelft.sem.template.matching.models;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Model representing the information needed for the Notification subsystem to email
 * the participant about the change of activity.
 */
@Data
@AllArgsConstructor
public class NotificationActivityModified {
    private String participantId;
    private long activityId;
}
