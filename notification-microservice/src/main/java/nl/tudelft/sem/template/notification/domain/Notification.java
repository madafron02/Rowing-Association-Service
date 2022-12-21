package nl.tudelft.sem.template.notification.domain;

import lombok.AllArgsConstructor;

import java.util.Objects;

/**
 * Notification object containing the email address of the receiver and its body (message).
 */
@AllArgsConstructor
public class Notification {
    private transient String receiverEmail;
    private transient String message;

    /**
     * Gets receiver email.
     *
     * @return the receiver email as String
     */
    public String getReceiverEmail() {
        return this.receiverEmail;
    }

    /**
     * Gets message.
     *
     * @return the message as String
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Check equality of notfications.
     *
     * @param o object to be checked if equal with this one
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notification)) {
            return false;
        }
        Notification that = (Notification) o;
        return Objects.equals(receiverEmail, that.receiverEmail) && Objects.equals(message, that.message);
    }

    /**
     * Hash code of notifications.
     *
     * @return hash code of the notification
     */
    @Override
    public int hashCode() {
        return Objects.hash(receiverEmail, message);
    }

    /**
     * Notification in string format.
     *
     * @return notification as string
     */
    @Override
    public String toString() {
        return "Notification{"
                + "receiverEmail='" + receiverEmail + '\''
                + ", message='" + message + '\''
                + '}';
    }
}
