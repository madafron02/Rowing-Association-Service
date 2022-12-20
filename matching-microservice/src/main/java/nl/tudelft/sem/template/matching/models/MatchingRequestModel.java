package nl.tudelft.sem.template.matching.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.matching.domain.TimeslotApp;

/**
 * Model representing a matching request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchingRequestModel {
    private TimeslotApp timeslot;
    private String position;
}
