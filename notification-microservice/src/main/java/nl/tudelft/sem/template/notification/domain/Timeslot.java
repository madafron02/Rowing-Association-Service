package nl.tudelft.sem.template.notification.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Timeslot {
    private final LocalDateTime start;
    private final LocalDateTime end;

    public Timeslot(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Timeslot)) return false;
        Timeslot timeslot = (Timeslot) o;
        return Objects.equals(start, timeslot.start) && Objects.equals(end, timeslot.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public String toString() {
        return "Timeslot{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
