package nl.tudelft.sem.template.matching.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

/**
 * A DDD value object representing an activity.
 */
@Getter
@NoArgsConstructor
public class ActivityApp {
    private long id;
    private String ownerId;
    private TimeslotApp timeslot;
    private HashMap<String, Integer> positions;
    @Setter
    private TypeOfActivity type;
    private ActivityProperties properties;

    /**
     * Constructor for ActivityApp.
     *
     * @param id of activity
     * @param ownerId id of the owner
     * @param timeslot timeslot of the activity
     * @param gender requested by the activity
     * @param organisation hosting the competition
     * @param positions available for the activity
     * @param competition true iff the activity is competition and requires competitive users
     * @param type of the activity TRAINING/COMPETITION
     * @param certificate required for the cox position of the activity
     */
    @JsonCreator
    public ActivityApp(@JsonProperty("id") long id,
                       @JsonProperty("ownerId") String ownerId,
                       @JsonProperty("timeslot") TimeslotApp timeslot,
                       @JsonProperty("gender") String gender,
                       @JsonProperty("organisation") String organisation,
                       @JsonProperty("positions") HashMap<String, Integer> positions,
                       @JsonProperty("competition") boolean competition,
                       @JsonProperty("type") TypeOfActivity type,
                       @JsonProperty("certificate") String certificate) {
        this.id = id;
        this.ownerId = ownerId;
        this.timeslot = timeslot;
        this.positions = positions;
        this.type = type;
        this.properties = new ActivityProperties(gender, organisation, competition, certificate);
    }


    /**
     * This method takes an activity and decides whether it is a Training or a Competition.
     *
     * @return the activity with the type set
     */
    public ActivityApp setTypeOfActivity() {
        if (properties.getGender() != null && properties.getOrganisation() != null) {
            setType(TypeOfActivity.COMPETITION);
        } else if (properties.getGender() == null && properties.getOrganisation() == null) {
            setType(TypeOfActivity.TRAINING);
        } else {
            return null;
        }
        return this;
    }
}

