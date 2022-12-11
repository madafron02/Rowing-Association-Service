package nl.tudelft.sem.template.notification.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import nl.tudelft.sem.template.notification.domain.Timeslot;

/**
 * Request model for the participant notification sending process when activity changes.
 */
@Data
@AllArgsConstructor
public class NotificationRequestModelParticipantChanges {
    private String participantId;
    private long activityId;
    private Timeslot timeslot;

    /**
     * Gets email of participant.
     *
     * @return email of participant
     */
    public String getParticipantId() {
        return participantId;
    }

    /**
     * Gets activity id.
     *
     * @return id of activity
     */
    public long getActivityId() {
        return activityId;
    }

    /**
     * Gets timeslot.
     *
     * @return timeslot chosen by participant
     */
    public Timeslot getTimeslot() {
        return timeslot;
    }
}
