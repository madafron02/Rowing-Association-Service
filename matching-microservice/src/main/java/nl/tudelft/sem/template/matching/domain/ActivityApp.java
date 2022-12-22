package nl.tudelft.sem.template.matching.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

/**
 * A DDD value object representing an activity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityApp {
    private long id;
    private String ownerId;
    private TimeslotApp timeslot;
    private String gender;
    private String organisation;
    private HashMap<String, Integer> positions;
    private boolean competition;
    private TypeOfActivity type;
    private String certificate;

    /**
     * This method takes an activity and decides whether it is a Training or a Competition.
     *
     * @return the activity with the type set
     */
    public ActivityApp setTypeOfActivity() {
        if(getGender() != null && getOrganisation() != null)
            setType(TypeOfActivity.COMPETITION);
        else if(getGender() == null && getOrganisation() == null)
            setType(TypeOfActivity.TRAINING);
        else
            return null;
        return this;
    }
}
