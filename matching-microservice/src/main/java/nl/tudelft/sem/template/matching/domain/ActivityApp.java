package nl.tudelft.sem.template.matching.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;

/**
 * A DDD value object representing an activity.
 */
@Data
@AllArgsConstructor
public class ActivityApp {
    private long activityId;
    private String publisherId;
    private TimeslotApp timeslot;
    private String gender;
    private String organisation;
    private HashMap<String, Integer> positions;
    private boolean competitiveness;
    private TypeOfActivity type;
    private String certificate;
}
