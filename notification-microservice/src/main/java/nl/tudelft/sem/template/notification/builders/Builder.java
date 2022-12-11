package nl.tudelft.sem.template.notification.builders;

import nl.tudelft.sem.template.notification.domain.Notification;
import org.springframework.stereotype.Component;

/**
 * Builder interface to be implemented by any concrete builder class.
 */
@Component
public interface Builder {
    /**
     * Setter for receiver email
     * @param receiverEmail email of the person who receives the notification
     */
    public void setReceiverEmail(String receiverEmail);

    /**
     * Setter for message
     * @param message the body of the notification
     */
    public void setMessage(String message);

    /**
     * Build method specific to this design pattern
     * @return a new Notification object
     */
    public Notification build();
}
