package nl.tudelft.sem.template.activities.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import nl.tudelft.sem.template.activities.domain.Activity;

/**
 * Model representing lists of activities.
 */
@Data
@AllArgsConstructor
public class ActivityListResponseModel {
    private List<Activity> activities;
}
