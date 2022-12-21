package nl.tudelft.sem.template.activities.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class TimeslotTest {

    private transient Timeslot timeslot;

    private LocalDateTime startTime = LocalDateTime.of(2042, 12, 12, 20, 15);

    private LocalDateTime endTime = LocalDateTime.of(2042, 12, 12, 23, 14);

    @BeforeEach
    void setUp() {
        timeslot = new Timeslot(startTime, endTime);
    }

    @Test
    void emptyConstructorTest() {
        assertThat(new Timeslot()).isNotNull();
    }

    @Test
    void constructorTest() {
        assertThat(timeslot).isNotNull();
    }

    @Test
    void getStartTime() {
        assertThat(timeslot.getStartTime()).isEqualTo(startTime);
    }

    @Test
    void getEndTime() {
        assertThat(timeslot.getEndTime()).isEqualTo(endTime);
    }

    @Test
    void setStartTime() {
        LocalDateTime newStartTime = LocalDateTime.of(2042, 12, 12, 19, 15);
        timeslot.setStartTime(newStartTime);
        assertThat(timeslot.getStartTime()).isEqualTo(newStartTime);
    }

    @Test
    void setEndTime() {
        LocalDateTime newEndTime = LocalDateTime.of(2042, 12, 12, 23, 15);
        timeslot.setEndTime(newEndTime);
        assertThat(timeslot.getEndTime()).isEqualTo(newEndTime);
    }

    @Test
    void equalsTrue() {
        LocalDateTime otherStartTime = LocalDateTime.of(2042, 12, 12, 20, 15);
        LocalDateTime otherEndTime = LocalDateTime.of(2042, 12, 12, 23, 14);
        Timeslot other = new Timeslot(otherStartTime, otherEndTime);
        assertThat(timeslot.equals(other)).isTrue();
    }

    @Test
    void equalsFalse() {
        LocalDateTime otherStartTime = LocalDateTime.of(2042, 12, 12, 19, 15);
        LocalDateTime otherEndTime = LocalDateTime.of(2042, 12, 12, 23, 14);
        Timeslot other = new Timeslot(otherStartTime, otherEndTime);
        assertThat(timeslot.equals(other)).isFalse();
    }

    @Test
    void hashCodeEquals() {
        LocalDateTime otherStartTime = LocalDateTime.of(2042, 12, 12, 20, 15);
        LocalDateTime otherEndTime = LocalDateTime.of(2042, 12, 12, 23, 14);
        Timeslot other = new Timeslot(otherStartTime, otherEndTime);
        assertThat(timeslot.hashCode()).isEqualTo(other.hashCode());
    }

    @Test
    void hashCodeNotEquals() {
        LocalDateTime otherStartTime = LocalDateTime.of(2042, 12, 12, 19, 15);
        LocalDateTime otherEndTime = LocalDateTime.of(2042, 12, 12, 23, 14);
        Timeslot other = new Timeslot(otherStartTime, otherEndTime);
        assertThat(timeslot.hashCode()).isNotEqualTo(other.hashCode());
    }
}