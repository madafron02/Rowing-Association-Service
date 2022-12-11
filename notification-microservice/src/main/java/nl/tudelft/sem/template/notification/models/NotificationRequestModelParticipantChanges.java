package nl.tudelft.sem.template.notification.models;

import nl.tudelft.sem.template.notification.domain.Timeslot;

public class NotificationRequestModelParticipantChanges {
    private String participantId;
    private long activityId;
    private Timeslot timeslot;

    public String getParticipantId() {
        return participantId;
    }

    public long getActivityId() {
        return activityId;
    }

    public Timeslot getTimeslot() {
        return timeslot;
    }
}
