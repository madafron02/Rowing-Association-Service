package nl.tudelft.sem.template.notification.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.notification.domain.Timeslot;

/**
 * Request model for the participant notification sending process.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestModelParticipant {
    private String participantId;
    private long activityId;
    private Timeslot timeslot;
    private boolean decision;
}


