package nl.tudelft.sem.template.notification.domain;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Timeslot object meant to specify start time and end time of an activity.
 */
@AllArgsConstructor
public class Timeslot {
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    /**
     * Gets start time.
     *
     * @return the start time
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Gets end time.
     *
     * @return the end time
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Check equality of timeslots.
     *
     * @param o the object to be checked if equal to this one
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Timeslot)) {
            return false;
        }
        Timeslot timeslot = (Timeslot) o;
        return Objects.equals(startTime, timeslot.startTime) && Objects.equals(endTime, timeslot.endTime);
    }

    /**
     * Gets hash code.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime);
    }

    /**
     * Gets timeslot as sting.
     *
     * @return timeslot as string
     */
    @Override
    public String toString() {
        return "Timeslot{"
                + "start=" + startTime
                + ", end=" + endTime
                + '}';
    }
}
