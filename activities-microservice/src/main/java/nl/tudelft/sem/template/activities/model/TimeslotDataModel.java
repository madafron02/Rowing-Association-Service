package nl.tudelft.sem.template.activities.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model representing a timeslot of an activity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeslotDataModel {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
