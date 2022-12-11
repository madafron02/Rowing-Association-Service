package nl.tudelft.sem.template.notification.builders;

import nl.tudelft.sem.template.notification.domain.Notification;

/**
 * NotificationBuilder is a concrete builder for notifications.
 */
public class NotificationBuilder implements Builder {
    private String receiverEmail;
    private String message;

    /**
     * Setter for receiver email.
     *
     * @param receiverEmail email of the person who receives the notification
     */
    @Override
    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    /**
     * Setter for message.
     *
     * @param message the body of the notification
     */
    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Build method specific to this design pattern.
     *
     * @return a new Notification object
     */
    @Override
    public Notification build() {
        return new Notification(receiverEmail, message);
    }
}
