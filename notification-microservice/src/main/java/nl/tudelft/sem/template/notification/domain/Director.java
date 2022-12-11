package nl.tudelft.sem.template.notification.domain;

import nl.tudelft.sem.template.notification.builders.Builder;

public class Director {
    private Builder builder;

    public Director(Builder builder) {
        this.builder = builder;
    }

    public void makeNotificationForPlayer(String participantId, long activityId,
                                                  Timeslot timeslot, boolean decision) {
        String message;

        if(decision) {
            message = "Congratulations! You have been accepted for activity " + activityId +
                    ". You are expected to be there between " + timeslot.getStart().toString()
                    + " and " + timeslot.getEnd().toString() + ".";
        } else {
            message = "Unfortunately, you have been denied for activity " + activityId +
                    ", happening between " + timeslot.getStart().toString() +
                    " and " + timeslot.getEnd().toString() +
                    ". We advise you to not give up and try another timeslot or activity.";
        }

        builder.setReceiverEmail(participantId);
        builder.setMessage(message);
    }

    public void makeNotificationForPlayerChanges(String participantId, long activityId,
                                          Timeslot timeslot) {
        builder.setReceiverEmail(participantId);
        builder.setMessage("Unfortunately, the details for activity " + activityId +
                ", happening between " + timeslot.getStart().toString() +
                " and " + timeslot.getEnd().toString() +
                "have been changed and you have been unenrolled. " +
                "We advise you to try another timeslot or activity.");
    }

    public void makeNotificationForPublisher(String ownerId, String participantId,
                                                 long activityId, Timeslot timeslot) {
        builder.setReceiverEmail(ownerId);
        builder.setMessage("You have a new request: user " +
                participantId + " wants to participate in activity " + activityId +
                " between " + timeslot.getStart().toString() + " and " +
                timeslot.getEnd().toString() + ". Please decide as soon as possible" +
                " whether you accept this request or not.");
    }
}
