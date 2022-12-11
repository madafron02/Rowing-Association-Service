package nl.tudelft.sem.template.notification.domain;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Timeslot object meant to specify start time and end time of an activity.
 */
public class Timeslot {
    private final LocalDateTime start;
    private final LocalDateTime end;

    /**
     * Constructor of a timeslot.
     *
     * @param start start time
     * @param end end time
     */
    public Timeslot(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Gets start time.
     *
     * @return the start time
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * Gets end time.
     *
     * @return the end time
     */
    public LocalDateTime getEnd() {
        return end;
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
        return Objects.equals(start, timeslot.start) && Objects.equals(end, timeslot.end);
    }

    /**
     * Gets hash code.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    /**
     * Gets timeslot as sting.
     *
     * @return timeslot as string
     */
    @Override
    public String toString() {
        return "Timeslot{"
                + "start=" + start
                + ", end=" + end
                + '}';
    }
}
