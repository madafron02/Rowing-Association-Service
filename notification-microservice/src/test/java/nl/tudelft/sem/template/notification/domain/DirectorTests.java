package nl.tudelft.sem.template.notification.domain;

import nl.tudelft.sem.template.notification.builders.Builder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;

public class DirectorTests {
    private transient Director director;
    private transient Builder builder;
    private transient String participantId = "test@gmail.com";
    private transient long activityId = 999;
    private transient Timeslot timeslot;
    private transient String dash = " - ";


    @BeforeEach
    public void setup() {
        builder = Mockito.mock(Builder.class);
        director = new Director(builder);
        timeslot = new Timeslot(LocalDateTime.now(), LocalDateTime.now().plusHours(1));
    }

    @Test
    public void makeNotificationForPlayerApprovedTest() {
        String expectedMessage = "Congratulations! You have been accepted for activity "
                + activityId + ". You are expected to be there between "
                + timeslot.getStart().toString()
                + dash + timeslot.getEnd().toString() + ".";

        director.makeNotificationForPlayer(participantId, activityId, timeslot, true);

        verify(builder).setMessage(expectedMessage);
        verify(builder).setReceiverEmail(participantId);
    }

    @Test
    public void makeNotificationForPlayerDeniedTest() {
        String expectedMessage = "Unfortunately, you have been denied for activity "
                + activityId + ", happening between "
                + timeslot.getStart().toString()
                + dash + timeslot.getEnd().toString()
                + ". We advise you to not give up and try another timeslot or activity.";

        director.makeNotificationForPlayer(participantId, activityId, timeslot, false);

        verify(builder).setMessage(expectedMessage);
        verify(builder).setReceiverEmail(participantId);
    }

    @Test
    public void makeNotificationForPlayerChangesTest() {
        String expectedMessage = "Unfortunately, the details for activity " + activityId
                + ", happening between "
                + timeslot.getStart().toString()
                + dash + timeslot.getEnd().toString()
                + "have been changed and you have been unenrolled. "
                + "We advise you to try another timeslot or activity.";

        director.makeNotificationForPlayerChanges(participantId, activityId, timeslot);

        verify(builder).setMessage(expectedMessage);
        verify(builder).setReceiverEmail(participantId);
    }

    @Test
    public void makeNotificationForPublisherTest() {
        String expectedOwnerId = "owner@gmail.com";
        String expectedMessage = "You have a new request: user "
                + participantId + " wants to participate in activity " + activityId
                + " between " + timeslot.getStart().toString()
                + dash + timeslot.getEnd().toString() + ". Please decide as soon as possible"
                + " whether you accept this request or not.";

        director.makeNotificationForPublisher(expectedOwnerId, participantId, activityId, timeslot);

        verify(builder).setMessage(expectedMessage);
        verify(builder).setReceiverEmail(expectedOwnerId);
    }
}
