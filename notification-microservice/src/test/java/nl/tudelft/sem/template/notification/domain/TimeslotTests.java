package nl.tudelft.sem.template.notification.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for Timeslot class.
 */
public class TimeslotTests {
    private transient Timeslot timeslot;
    private transient Timeslot sameTimeslot;
    private transient Timeslot otherTimeslot;
    private transient LocalDateTime start;
    private transient LocalDateTime end;

    /**
     * General setup for tests.
     */
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
        assertThat(timeslot.getStartTime()).isEqualTo(start);
    }

    @Test
    public void getEndTest() {
        assertThat(timeslot.getEndTime()).isEqualTo(end);
    }
}
