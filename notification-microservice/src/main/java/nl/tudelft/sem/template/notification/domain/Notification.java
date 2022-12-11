package nl.tudelft.sem.template.notification.domain;

import java.util.Objects;

public class Notification {
    private String receiverEmail;
    private String message;

    public Notification(String receiverEmail, String message) {
        this.receiverEmail = receiverEmail;
        this.message = message;
    }

    public String getReceiverEmail() {
        return this.receiverEmail;
    }

    public String getMessage() {
        return this.message;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notification)) return false;
        Notification that = (Notification) o;
        return Objects.equals(receiverEmail, that.receiverEmail) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(receiverEmail, message);
    }

    @Override
    public String toString() {
        return "Notification{" +
                "receiverEmail='" + receiverEmail + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
