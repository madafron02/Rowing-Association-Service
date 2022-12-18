package nl.tudelft.sem.template.activities.model;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * Model representing a timeslot of an activity.
 */
@Data
public class TimeslotDataModel {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
