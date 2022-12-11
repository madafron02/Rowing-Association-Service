package nl.tudelft.sem.template.notification.domain;

import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Notification object containing the email address of the receiver and its body (message)
 */
@Component
public class Notification {
    private String receiverEmail;
    private String message;

    /**
     * Constructor of a notification
     *
     * @param receiverEmail email of receiver
     * @param message email body
     */
    public Notification(String receiverEmail, String message) {
        this.receiverEmail = receiverEmail;
        this.message = message;
    }

    /**
     * @return the receiver email as String
     */
    public String getReceiverEmail() {
        return this.receiverEmail;
    }

    /**
     * @return the message as String
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * @param o object to be checked if equal with this one
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notification)) return false;
        Notification that = (Notification) o;
        return Objects.equals(receiverEmail, that.receiverEmail) && Objects.equals(message, that.message);
    }

    /**
     * @return hash code of the notification
     */
    @Override
    public int hashCode() {
        return Objects.hash(receiverEmail, message);
    }

    /**
     * @return notification as string
     */
    @Override
    public String toString() {
        return "Notification{" +
                "receiverEmail='" + receiverEmail + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
