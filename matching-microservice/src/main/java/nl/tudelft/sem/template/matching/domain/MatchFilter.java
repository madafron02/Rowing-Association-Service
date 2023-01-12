package nl.tudelft.sem.template.matching.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import nl.tudelft.sem.template.matching.models.UserPreferences;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class MatchFilter {
    private ActivityApp activityApp;
    private UserPreferences userPreferences;
}
