package nl.tudelft.sem.template.matching.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * A DDD value object representing a user.
 */
@Data
@AllArgsConstructor
public class UserApp {
    private String id;
    private String certificate;
    private String gender;
    private String organisation;
    private boolean competitiveness;
}
