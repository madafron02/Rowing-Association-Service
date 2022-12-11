package nl.tudelft.sem.template.notification.models;

import nl.tudelft.sem.template.notification.domain.Timeslot;

public class NotificationRequestModelOwner {
    private String ownerId;
    private String participantId;
    private long activityId;
    private Timeslot timeslot;

    public String getOwnerId() {
        return ownerId;
    }

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


