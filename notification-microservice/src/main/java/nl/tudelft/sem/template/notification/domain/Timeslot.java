package nl.tudelft.sem.template.notification.domain;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Timeslot object meant to specify start time and end time of an activity
 */
@Component
public class Timeslot {
    private final LocalDateTime start;
    private final LocalDateTime end;

    /**
     * Constructor of a timeslot
     *
     * @param start start time
     * @param end end time
     */
    public Timeslot(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    /**
     * @return the start time
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * @return the end time
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * @param o the object to be checked if equal to this one
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Timeslot)) return false;
        Timeslot timeslot = (Timeslot) o;
        return Objects.equals(start, timeslot.start) && Objects.equals(end, timeslot.end);
    }

    /**
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    /**
     * @return timeslot as string
     */
    @Override
    public String toString() {
        return "Timeslot{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
