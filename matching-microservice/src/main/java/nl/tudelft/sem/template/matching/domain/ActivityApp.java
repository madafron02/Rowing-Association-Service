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
}
