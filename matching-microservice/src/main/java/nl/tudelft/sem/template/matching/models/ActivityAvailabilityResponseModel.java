package nl.tudelft.sem.template.matching.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.matching.domain.ActivityApp;

import java.util.List;

/**
 * Model representing a list of available activties as response to the Activity Microservice request.
 */
@Data
@NoArgsConstructor
public class ActivityAvailabilityResponseModel {

    private List<ActivityApp> availableActivities;
}
