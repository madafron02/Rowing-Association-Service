package nl.tudelft.sem.template.notification.builders;

import nl.tudelft.sem.template.notification.domain.Notification;

public interface Builder {
    public void setReceiverEmail(String receiverEmail);
    public void setMessage(String message);
    public Notification build();
}
