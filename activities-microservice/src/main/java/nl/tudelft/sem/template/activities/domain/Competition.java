package nl.tudelft.sem.template.activities.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A DDD entity representing a competition in our domain.
 */
@Entity
@Table(name = "competitions")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class Competition extends Training {

    public static final List<String> GENDER_TYPES = List.of("Male", "Female", "Other");

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "competitiveness", nullable = false)
    private boolean competitiveness;

    @Column(name = "organisation", nullable = false)
    private String organisation;

    /**
     * Creates a new Competition.
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
     * @param gender the gender of the needed participants, null if not needed
     * @param competitiveness true for competitive, false for amateur
     * @param organisation the organisation of the team
     */
    public Competition(String ownerId, Integer coxCount, Integer coachCount, Integer portSideRowerCount,
                       Integer starboardSideRowerCount, Integer scullingRowerCount, LocalDateTime startTime,
                       LocalDateTime endTime, String certificate, String gender, boolean competitiveness,
                       String organisation) {
        super(ownerId, coxCount, coachCount, portSideRowerCount, starboardSideRowerCount,
                scullingRowerCount, startTime, endTime, certificate);
        this.gender = gender;
        this.competitiveness = competitiveness;
        this.organisation = organisation;
    }

    /**
     * Checks if this activity has valid data.
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
        if (gender == null || !GENDER_TYPES.contains(gender)) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        boolean timeslotExists = timeslot != null && timeslot.getStartTime() != null && timeslot.getEndTime() != null;
        return timeslotExists && timeslot.getStartTime().isBefore(timeslot.getEndTime())
                && timeslot.getEndTime().isAfter(now);
    }
}
