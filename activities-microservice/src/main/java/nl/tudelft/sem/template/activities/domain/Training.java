package nl.tudelft.sem.template.activities.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
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

/**
 * A DDD entity representing a training in our domain.
 */
@Entity
@Table(name = "trainings")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class Training {

    public static final List<String> CERTIFICATE_TYPES = List.of("C4", "4+", "8+");

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
     * @param coxCount number of needed coxes
     * @param coachCount number of needed coaches
     * @param portSideRowerCount number of needed port side rowers
     * @param starboardSideRowerCount number of needed starboard side
     * @param scullingRowerCount number of needed sculling rowers
     * @param startTime starting time of the activity
     * @param endTime ending time of the activity
     * @param certificate the boat type
     */
    public Training(String ownerId, Integer coxCount, Integer coachCount, Integer portSideRowerCount,
                    Integer starboardSideRowerCount, Integer scullingRowerCount, LocalDateTime startTime,
                    LocalDateTime endTime, String certificate) {
        this.ownerId = ownerId;
        this.positions = new Positions(coxCount, coachCount, portSideRowerCount, starboardSideRowerCount,
                scullingRowerCount);
        this.timeslot = new Timeslot(startTime, endTime);
        this.certificate = certificate;
    }

    /**
     * Checks if this training has valid data.
     *
     * @return true if this is valid and false otherwise
     */
    public boolean checkIfValid() {
        boolean requiresRowers = positions.getCoxCount() != null || positions.getCoachCount() != null
                || positions.getPortSideRowerCount() != null || positions.getStarboardSideRowerCount() != null
                || positions.getScullingRowerCount() != null;
        boolean nonNull = ownerId != null && requiresRowers;
        if (!nonNull) {
            return false;
        }
        if (!CERTIFICATE_TYPES.contains(certificate)) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        boolean timeslotExists = timeslot != null && timeslot.getStartTime() != null && timeslot.getEndTime() != null;
        return timeslotExists && timeslot.getStartTime().isBefore(timeslot.getEndTime())
                && timeslot.getEndTime().isAfter(now);
    }
}

