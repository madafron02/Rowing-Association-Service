package nl.tudelft.sem.template.notification.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import nl.tudelft.sem.template.notification.domain.Timeslot;

/**
 * Request model for the participant notification sending process
 */
@Data
@AllArgsConstructor
public class NotificationRequestModelParticipant {
    private String participantId;
    private long activityId;
    private Timeslot timeslot;
    private boolean decision;

    /**
     * @return email of participant
     */
    public String getParticipantId() {
        return participantId;
    }

    /**
     * @return id of activity
     */
    public long getActivityId() {
        return activityId;
    }

    /**
     * @return timeslot chosen bu participant
     */
    public Timeslot getTimeslot() {
        return timeslot;
    }

    /**
     * @return decision of owner
     */
    public boolean isDecision() {
        return decision;
    }
}


