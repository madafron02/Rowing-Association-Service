package nl.tudelft.sem.template.activities.domain;

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
     * @param cox number of needed coxes
     * @param coach number of needed coaches
     * @param port number of needed port side rowers
     * @param starboard number of needed starboard side
     * @param sculling number of needed sculling rowers
     * @param timeslot the timeslot of a training
     * @param certificate the boat type
     * @param gender the gender of the needed participants, null if not needed
     * @param competitiveness true for competitive, false for amateur
     * @param organisation the organisation of the team
     */
    public Competition(String ownerId, Integer cox, Integer coach, Integer port,
                       Integer starboard, Integer sculling, Timeslot timeslot, String certificate,
                       String gender, boolean competitiveness, String organisation) {
        super(ownerId, cox, coach, port, starboard,
                sculling, timeslot, certificate);
        this.gender = gender;
        this.competitiveness = competitiveness;
        this.organisation = organisation;
    }

    /**
     * Checks if this competition has valid data.
     *
     * @return true if this is valid and false otherwise
     */
    public boolean checkIfValid() {
        if (gender == null || !GENDER_TYPES.contains(gender)) {
            return false;
        }
        return super.checkIfValid();
    }
}
