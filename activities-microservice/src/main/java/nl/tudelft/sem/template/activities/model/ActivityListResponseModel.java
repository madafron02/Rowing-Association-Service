package nl.tudelft.sem.template.activities.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import nl.tudelft.sem.template.activities.domain.Competition;
import nl.tudelft.sem.template.activities.domain.Training;

/**
 * Model representing lists of activities.
 */
@Data
@AllArgsConstructor
public class ActivityListResponseModel {
    private List<Training> activities;
}
