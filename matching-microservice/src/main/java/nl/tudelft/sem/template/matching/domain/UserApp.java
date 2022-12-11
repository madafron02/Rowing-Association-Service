package nl.tudelft.sem.template.matching.domain;

import lombok.Data;

/**
 * A DDD value object representing a user.
 */
@Data
public class UserApp {
    private String id;
    private String certificate;
    private String gender;
    private String organisation;
    private boolean competitiveness;
}
