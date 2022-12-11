package nl.tudelft.sem.template.notification.builders;

import nl.tudelft.sem.template.notification.domain.Notification;

public class NotificationBuilder implements Builder{
    private String receiverEmail;
    private String message;

    @Override
    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public Notification build() {
        return new Notification(receiverEmail, message);
    }
}
