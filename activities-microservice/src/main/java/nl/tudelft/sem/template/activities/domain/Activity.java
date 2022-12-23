package nl.tudelft.sem.template.activities.domain;

import java.time.LocalDateTime;
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
     * @param coxCount number of needed coxes
     * @param coachCount number of needed coaches
     * @param portSideRowerCount number of needed port side rowers
     * @param starboardSideRowerCount number of needed starboard side
     * @param scullingRowerCount number of needed sculling rowers
     * @param startTime starting time of the activity
     * @param endTime ending time of the activity
     * @param certificate the boat type
     * @param competition true if it is a competition and false otherwise
     * @param gender the gender of the needed participants, null if not needed
     * @param organisation the origanisation of the participant
     */
    public Activity(String ownerId, Integer coxCount, Integer coachCount, Integer portSideRowerCount,
                    Integer starboardSideRowerCount, Integer scullingRowerCount, LocalDateTime startTime,
                    LocalDateTime endTime, String certificate, Boolean competition, String gender, String organisation) {
        this.ownerId = ownerId;
        this.positions = new Positions(coxCount, coachCount, portSideRowerCount, starboardSideRowerCount,
                scullingRowerCount);
        this.timeslot = new Timeslot(startTime, endTime);
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
        boolean requiresRowers = positions.getCox() != null || positions.getCoach() != null
                || positions.getPort() != null || positions.getStarboard() != null
                || positions.getSculling() != null;
        boolean nonNull = ownerId != null && requiresRowers;
        if (!nonNull) {
            return false;
        }
        if (!CERTIFICATE_TYPES.contains(certificate)) {
            return false;
        }
        if (competition && ((gender == null || !GENDER_TYPES.contains(gender)) || organisation == null)) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        boolean timeslotExists = timeslot != null && timeslot.getStartTime() != null && timeslot.getEndTime() != null;
        return timeslotExists && timeslot.getStartTime().isBefore(timeslot.getEndTime())
                && timeslot.getEndTime().isAfter(now);
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
