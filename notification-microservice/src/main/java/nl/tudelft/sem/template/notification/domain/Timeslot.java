package nl.tudelft.sem.template.notification.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Timeslot object meant to specify start time and end time of an activity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Timeslot {
    private transient LocalDateTime startTime;
    private transient LocalDateTime endTime;
}
