package nl.tudelft.sem.template.activities.model;

import lombok.Data;
import nl.tudelft.sem.template.activities.domain.Positions;
import nl.tudelft.sem.template.activities.domain.Timeslot;

/**
 * Model representing data used for updating an activity.
 */
@Data
public class UpdateRequestDataModel {

    Long id;

    Positions positions;

    Timeslot timeslot;

    String certificate;

    String gender;

    Boolean competitiveness;

    String organisation;
}
