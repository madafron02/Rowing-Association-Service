package nl.tudelft.sem.template.matching.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DDD value object representing a user.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserApp {
    private String email;
    private String certificate;
    private String gender;
    private String organisation;
    private boolean competitive;
}
