package nl.tudelft.sem.template.matching.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * A DDD value object representing a timeslot.
 */
@AllArgsConstructor
@EqualsAndHashCode
public class TimeslotApp {

    private final LocalDateTime start;

    private final LocalDateTime end;

    /**
     * Getter for start time.
     *
     * @return start time
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * Getter for end time.
     *
     * @return end time
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * Method that generates a String containing start and end time.
     *
     * @return String form of TimeslotApp
     */
    @Override
    public String toString() {
        return getStart().toString() + "--" + getEnd().toString();
    }
}
