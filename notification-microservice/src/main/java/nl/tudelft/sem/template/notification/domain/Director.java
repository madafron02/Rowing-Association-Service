package nl.tudelft.sem.template.notification.domain;

import nl.tudelft.sem.template.notification.builders.Builder;
import org.springframework.stereotype.Component;

/**
 * The director corresponding to the builder design pattern, it decides which scenario to use
 * when building a concrete Notification object
 */
@Component
public class Director {
    private Builder builder;

    /**
     * Constructor of the director
     *
     * @param builder chosen builder
     */
    public Director(Builder builder) {
        this.builder = builder;
    }

    /**
     * Sets values of the builder when constructing a notification for the player
     * in case he has a response from the owner of the activity he signed up for
     *
     * @param participantId email of the participant
     * @param activityId id of the activity
     * @param timeslot start and end time of the activity
     * @param decision decision from activity owner
     */
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

    /**
     * Sets values of the builder when constructing a notification for the player
     * in case the activity he signed up for gets cancelled or its details are changed
     *
     * @param participantId email of the participant
     * @param activityId id of the activity
     * @param timeslot start and end time of the activity
     */
    public void makeNotificationForPlayerChanges(String participantId, long activityId,
                                          Timeslot timeslot) {
        builder.setReceiverEmail(participantId);
        builder.setMessage("Unfortunately, the details for activity " + activityId +
                ", happening between " + timeslot.getStart().toString() +
                " and " + timeslot.getEnd().toString() +
                "have been changed and you have been unenrolled. " +
                "We advise you to try another timeslot or activity.");
    }

    /**
     * Sets values of the builder when constructing a notification for the owner
     * of an activity in case a player requested to sign up for it
     *
     * @param ownerId email of the owner
     * @param participantId email of the participant
     * @param activityId id of the activity
     * @param timeslot start and end time of the activity
     */
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
