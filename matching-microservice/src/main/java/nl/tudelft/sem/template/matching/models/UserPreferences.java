package nl.tudelft.sem.template.matching.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.matching.domain.TimeslotApp;
import nl.tudelft.sem.template.matching.domain.UserApp;

/**
 * Model representing the preferences of an user.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPreferences {

    private TimeslotApp timeslot;
    private UserApp user;
    private String position;
}
