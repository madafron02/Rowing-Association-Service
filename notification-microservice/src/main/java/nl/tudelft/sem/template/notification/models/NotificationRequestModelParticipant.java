package nl.tudelft.sem.template.notification.models;

import nl.tudelft.sem.template.notification.domain.Timeslot;

public class NotificationRequestModelParticipant {
    private String participantId;
    private long activityId;
    private Timeslot timeslot;
    private boolean decision;

    public String getParticipantId() {
        return participantId;
    }

    public long getActivityId() {
        return activityId;
    }

    public Timeslot getTimeslot() {
        return timeslot;
    }

    public boolean isDecision() {
        return decision;
    }
}


