package nl.tudelft.sem.template.matching.models;

import lombok.Data;
import nl.tudelft.sem.template.matching.domain.TimeslotApp;

/**
 * Model representing a matching request.
 */
@Data
public class MatchingRequestModel {
    private TimeslotApp timeslot;
    private String position;
}
