package nl.tudelft.sem.template.notification.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for Notification class.
 */
public class NotificationTests {
    private transient Notification notification;
    private transient String receiverEmail = "test@gmail.com";
    private transient String message = "test message";
    private transient Notification sameNotification = new Notification(receiverEmail, message);
    private transient Notification otherNotification =
            new Notification("other@gmail.com", "other message");

    /**
     * General setup for tests.
     */
    @BeforeEach
    public void setup() {
        notification = new Notification(receiverEmail, message);
    }

    @Test
    public void constructorTest() {
        assertThat(notification).isNotNull();
    }

    @Test
    public void getReceiverEmailTest() {
        assertThat(notification.getReceiverEmail()).isEqualTo(receiverEmail);
    }

    @Test
    public void getMessageTest() {
        assertThat(notification.getMessage()).isEqualTo(message);
    }

    @Test
    public void hashCodeTest() {
        int actualHashCode = notification.hashCode();

        assertThat(actualHashCode).isEqualTo(notification.hashCode());
        assertThat(actualHashCode).isEqualTo(sameNotification.hashCode());
        assertThat(actualHashCode).isNotEqualTo(otherNotification.hashCode());
    }

    @Test
    public void equalsTest() {
        assertThat(notification).isEqualTo(notification);
        assertThat(notification).isEqualTo(sameNotification);
        assertThat(notification).isNotEqualTo(otherNotification);
    }

    @Test
    public void toStringTest() {
        assertThat(notification.toString()).isEqualTo("Notification{"
                + "receiverEmail='" + receiverEmail + '\''
                + ", message='" + message + '\''
                + '}');
    }
}
