package nl.tudelft.sem.template.activities.domain;

import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Embedded;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A DDD entity representing an activity in our domain.
 */
@Entity
@Table(name = "activities")
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class Activity {

    public static final List<String> CERTIFICATE_TYPES = List.of("C4", "4+", "8+");

    public static final List<String> GENDER_TYPES = List.of("Male", "Female");

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "ownerId", nullable = false)
    private String ownerId;

    @Embedded
    private Positions positions;

    @Embedded
    private Timeslot timeslot;

    @Column(name = "certificate")
    private String certificate;

    @Column(name = "competition", nullable = false)
    private Boolean competition;

    @Column(name = "gender")
    private String gender;

    @Column(name = "organisation")
    private String organisation;

    /**
     * Creates a new Activity.
     *
     * @param ownerId the email of the user that created the activity
     * @param positions the positions of the needed participants
     * @param timeslot the timeslot representing the time of the activity
     * @param certificate the boat type
     * @param competition true if it is a competition and false otherwise
     * @param gender the gender of the needed participants, null if not needed
     * @param organisation the origanisation of the participant
     */
    public Activity(String ownerId, Positions positions, Timeslot timeslot, String certificate,
                    Boolean competition, String gender, String organisation) {
        this.ownerId = ownerId;
        this.positions = positions;
        this.timeslot = timeslot;
        this.certificate = certificate;
        this.competition = Objects.requireNonNullElse(competition, false);
        this.gender = gender;
        this.organisation = organisation;
    }

    /**
     * Checks if this activity has valid data.
     *
     * @return true if this is valid and false otherwise
     */
    public boolean checkIfValid() {
        boolean valid = ownerId != null && CERTIFICATE_TYPES.contains(certificate);
        return valid && checkIfPositionsAndTimeslotValid() && checkIfCompetitionValid();
    }

    /**
     * Checks if the positions and timeslot have valid data.
     *
     * @return true if the data is valid and false otherwise
     */
    public boolean checkIfPositionsAndTimeslotValid() {
        return positions != null && positions.checkIfValid() && timeslot != null && timeslot.checkIfValid();
    }

    /**
     * Checks if this activity is a competition that is valid.
     *
     * @return true if this is a valid competition or not a competition at all and false otherwise
     */
    public boolean checkIfCompetitionValid() {
        return !competition || ((gender == null || GENDER_TYPES.contains(gender)) && organisation != null);
    }

    /**
     * Updates the values of this Activity with the values from another Activity.
     *
     * @param other the Activity that contains the values to update
     */
    public void updateFields(Activity other) {
        if (other.getPositions() != null) {
            this.setPositions(other.getPositions());
        }
        if (other.getTimeslot() != null) {
            this.setTimeslot(other.getTimeslot());
        }
        if (other.getCertificate() != null) {
            this.setCertificate(other.getCertificate());
        }
        if (other.getCompetition() != null) {
            this.setCompetition(other.getCompetition());
        }
        if (other.getGender() != null) {
            this.setGender(other.getGender());
        }
    }
}
