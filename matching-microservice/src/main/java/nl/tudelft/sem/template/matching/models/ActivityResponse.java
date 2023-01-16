package nl.tudelft.sem.template.matching.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.tudelft.sem.template.matching.domain.TimeslotApp;
import nl.tudelft.sem.template.matching.domain.TypeOfActivity;

/**
 * Model representing an activity response.
 */
@Getter
@Setter
@NoArgsConstructor
public class ActivityResponse {
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
    public ActivityResponse(long matchId, TypeOfActivity type, TimeslotApp timeslot) {
        this.matchId = matchId;
        this.type = type;
        this.timeslot = timeslot;
    }
}
