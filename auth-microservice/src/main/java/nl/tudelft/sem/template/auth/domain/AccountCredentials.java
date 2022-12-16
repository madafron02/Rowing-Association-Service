package nl.tudelft.sem.template.auth.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

/**
 * Entity that stores a userId and password.
 */
@Entity
public class AccountCredentials {
    @Id
    private String userId;
    private String password;

    /**
     * Constructs an AccountCredentials entity.
     *
     * @param userId The userId as a String.
     * @param password The password as a String.
     */
    public AccountCredentials(String userId, String password){
        this.userId = userId;
        this.password = password;
    }

    /**
     * Empty Constructor for an AccountCredentials entity.
     */
    public AccountCredentials() {

    }

    /**
     * Gets the userId.
     *
     * @return The userId as a String.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Gets the password.
     *
     * @return The password as a String.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Checks if two AccountCredentials entities are the same.
     *
     * @param o The other AccountCredentials object for this to be compared to.
     * @return True if the userId and password are the same. False otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountCredentials)) {
            return false;
        }
        AccountCredentials that = (AccountCredentials) o;
        return Objects.equals(getUserId(), that.getUserId()) && Objects.equals(getPassword(), that.getPassword());
    }

    /**
     * Creates a hashcode from an AccountCredentials object.
     *
     * @return The hashcode generated for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getPassword());
    }

    /**
     * Creates a formatted String for this AccountCredentials object.
     *
     * @return The object as a formatted String.
     */
    @Override
    public String toString() {
        return "AccountCredentials{"
                + "userId='" + userId + '\''
                + ", password='" + password + '\''
                + '}';
    }
}
