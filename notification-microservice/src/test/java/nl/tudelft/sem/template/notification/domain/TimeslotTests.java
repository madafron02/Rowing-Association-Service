package nl.tudelft.sem.template.notification.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeslotTests {
    private transient Timeslot timeslot;
    private transient Timeslot sameTimeslot;
    private transient Timeslot otherTimeslot;
    private transient LocalDateTime start;
    private transient LocalDateTime end;

    @BeforeEach
    public void setup() {
        start = LocalDateTime.now();
        end = LocalDateTime.now().plusHours(1);
        timeslot = new Timeslot(start, end);
        sameTimeslot = new Timeslot(start, end);
        otherTimeslot = new Timeslot(LocalDateTime.now().plusHours(2),
                LocalDateTime.now().plusHours(3));
    }

    @Test
    public void constructorTest() {
        assertThat(timeslot).isNotNull();
    }

    @Test
    public void getStartTest() {
        assertThat(timeslot.getStart()).isEqualTo(start);
    }

    @Test
    public void getEndTest() {
        assertThat(timeslot.getEnd()).isEqualTo(end);
    }

    @Test
    public void hashCodeTest() {
        int actualHashCode = timeslot.hashCode();

        assertThat(actualHashCode).isEqualTo(timeslot.hashCode());
        assertThat(actualHashCode).isEqualTo(sameTimeslot.hashCode());
        assertThat(actualHashCode).isNotEqualTo(otherTimeslot.hashCode());
    }

    @Test
    public void equalsTest() {
        assertThat(timeslot).isEqualTo(timeslot);
        assertThat(timeslot).isEqualTo(sameTimeslot);
        assertThat(timeslot).isNotEqualTo(otherTimeslot);
    }

    @Test
    public void toStringTest() {
        assertThat(timeslot.toString()).isEqualTo("Timeslot{"
                + "start=" + start
                + ", end=" + end
                + '}');
    }
}
