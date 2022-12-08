package nl.tudelft.sem.template.matching.models;

import lombok.Data;
import nl.tudelft.sem.template.matching.domain.TimeslotApp;

/**
 * Model representing an activity response.
 */
@Data
public class ActivityReponse {
    private long matchId;
    private String type;
    private TimeslotApp timeslot;

}
