package nl.tudelft.sem.template.activities.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Embedded;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.tudelft.sem.template.activities.model.UpdateRequestDataModel;

/**
 * A DDD entity representing a training in our domain.
 */
@Entity
@Table(name = "trainings")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "TYPE")
@DiscriminatorValue("TRAINING")
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "ownerId", nullable = false)
    protected String ownerId;

    @Embedded
    protected Positions positions;

    @Embedded
    protected Timeslot timeslot;

    @Column(name = "certificate")
    protected String certificate;

    /**
     * Creates a new Training.
     *
     * @param ownerId the email of the user that created the activity
     * @param cox number of needed coxes
     * @param coach number of needed coaches
     * @param port number of needed port side rowers
     * @param starboard number of needed starboard side
     * @param sculling number of needed sculling rowers
     * @param timeslot the timeslot of a Training
     * @param certificate the boat type
     */
    public Training(String ownerId, Integer cox, Integer coach, Integer port,
                    Integer starboard, Integer sculling, Timeslot timeslot, String certificate) {
        this.ownerId = ownerId;
        this.positions = new Positions(cox, coach, port, starboard,
                sculling);
        this.timeslot = timeslot;
        this.certificate = certificate;
    }

    /**
     * Updates the values of this Training with the values from another Training.
     *
     * @param other the Training that contains the values to update
     */
    public void updateFields(UpdateRequestDataModel other) {
        if (other.getPositions() != null) {
            this.setPositions(other.getPositions());
        }
        if (other.getTimeslot() != null) {
            this.setTimeslot(other.getTimeslot());
        }
        if (other.getCertificate() != null) {
            this.setCertificate(other.getCertificate());
        }
    }

    /**
     * Checks if this training has valid data.
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
        if (certificate == null || certificate.equals("")) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        boolean timeslotExists = timeslot != null && timeslot.getStartTime() != null && timeslot.getEndTime() != null;
        return timeslotExists && timeslot.getStartTime().isBefore(timeslot.getEndTime())
                && timeslot.getEndTime().isAfter(now);
    }
}
