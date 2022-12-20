package nl.tudelft.sem.template.users.domain;

import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "Users")
@NoArgsConstructor
public class User {

    /**
     * Entity that represents the users in the system.
     */

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String email;
    private String gender;
    private boolean competitiveness;
    private String certificate;
    private String organisation;

    /**
     * Constructor for the User entity.
     *
     * @param email the email of the user
     * @param gender the gender of the user
     * @param competitive TRUE if user is competitive, FALSE if user is not competitive
     * @param certificate the highest priority certificate of the user
     * @param organization the organization of the user
     */
    public User(String email, String gender, boolean competitiveness, String certificate, String organization) {
        this.email = email;
        this.gender = gender;
        this.competitiveness = competitiveness;
        this.certificate = certificate;
        this.organisation = organization;
    }

    /**
     * Constructor for the User entity, only defining the id.
     *
     * @param email the email of the user
     */
    public User(String email) {
        this.email = email;
    }

    /**
     * Getter for the email of a user.
     *
     * @return the email of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Getter for the gender of a user.
     *
     * @return the gender of the user
     */
    public String getGender() {
        return gender;
    }

    /**
     * Getter for the competitiveness of a user.
     *
     * @return TRUE if the user is competitive, FALSE if the user is not competitive
     */
    public boolean isCompetitive() {
        return competitiveness;
    }

    /**
     * Getter for the certificate of a user.
     *
     * @return the certificate of a user
     */
    public String getCertificate() {
        return certificate;
    }

    /**
     * Getter for the organization of a user.
     *
     * @return the organization of a user
     */
    public String getOrganisation() {
        return organisation;
    }

    /**
     * Setter for the gender of a user.
     *
     * @param gender the gender of the user
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Setter for the competitiveness of a user.
     *
     * @param competitiveness the competitiveness of the user
     */
    public void setCompetitiveness(boolean competitiveness) {
        this.competitiveness = competitiveness;
    }

    /**
     * Setter for the certificate of a user.
     *
     * @param certificate the certificate of a user
     */
    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    /**
     * Setter for the organization of a user.
     *
     * @param organisation the organization of a user
     */
    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    /**
     * Method for checking equality between two User objects, only considers the User's email.
     *
     * @param o the comparison object
     * @return TRUE if the objects are equal. FALSE if they are not equal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return email.equals(user.email);
    }

    /**
     * Method for hashing the User entity, only consider the user's email.
     *
     * @return hashCode of User entity
     */
    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
