package nl.tudelft.sem.template.notification.builders;

import nl.tudelft.sem.template.notification.domain.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for NotificationBuilder class.
 */
public class NotificationBuilderTests {
    private transient NotificationBuilder notificationBuilder;
    private transient String testEmail = "test@gmail.com";
    private transient String testMessage = "test message";

    /**
     * General setup for tests.
     */
    @BeforeEach
    public void setup() {
        notificationBuilder = new NotificationBuilder();
    }

    @Test
    public void constructorTest() {
        assertThat(notificationBuilder).isNotNull();
    }

    @Test
    public void setReceiverEmailTest() throws NoSuchFieldException, IllegalAccessException {
        notificationBuilder.setReceiverEmail(testEmail);
        Field field = notificationBuilder.getClass().getDeclaredField("receiverEmail");
        field.setAccessible(true);
        assertThat(field.get(notificationBuilder)).isEqualTo(testEmail);
    }

    @Test
    public void setMessageTest() throws NoSuchFieldException, IllegalAccessException {
        notificationBuilder.setMessage(testMessage);
        Field field = notificationBuilder.getClass().getDeclaredField("message");
        field.setAccessible(true);
        assertThat(field.get(notificationBuilder)).isEqualTo(testMessage);
    }

    @Test
    public void buildTest() {
        Notification expected = new Notification(testEmail, testMessage);
        notificationBuilder.setReceiverEmail(testEmail);
        notificationBuilder.setMessage(testMessage);
        assertThat(notificationBuilder.build()).isEqualTo(expected);
    }
}
