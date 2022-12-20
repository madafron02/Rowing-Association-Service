package nl.tudelft.sem.template.matching.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * A DDD value object representing a timeslot.
 */
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@Data
public class TimeslotApp {

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    /**
     * Method that generates a String containing start and end time.
     *
     * @return String form of TimeslotApp
     */
    @Override
    public String toString() {
        return getStartTime().toString() + "--" + getEndTime().toString();
    }
}
