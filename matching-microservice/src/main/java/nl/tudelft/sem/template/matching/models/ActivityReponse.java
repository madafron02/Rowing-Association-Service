package nl.tudelft.sem.template.matching.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.matching.domain.TimeslotApp;
import nl.tudelft.sem.template.matching.domain.TypeOfActivity;

/**
 * Model representing an activity response.
 */
@Data
@NoArgsConstructor
public class ActivityReponse {
    private long matchId;
    private TypeOfActivity type;
    private TimeslotApp timeslot;

    /**
     * Constructor of the ActivityResponse model.
     *
     * @param matchId  the id of the match created
     * @param type     the type of activity (competition/training)
     * @param timeslot the timeslot of the activity
     */
    public ActivityReponse(long matchId, TypeOfActivity type, TimeslotApp timeslot) {
        this.matchId = matchId;
        this.type = type;
        this.timeslot = timeslot;
    }
}
