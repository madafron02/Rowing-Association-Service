package nl.tudelft.sem.template.notification.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.notification.domain.Timeslot;

/**
 * Request model for the owner notification sending process.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestModelOwner {
    private String ownerId;
    private String participantId;
    private long activityId;
    private Timeslot timeslot;
}


