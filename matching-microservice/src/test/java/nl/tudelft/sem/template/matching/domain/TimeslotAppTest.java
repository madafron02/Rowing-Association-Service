package nl.tudelft.sem.template.matching.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class TimeslotAppTest {
    TimeslotApp timeslot;

    @BeforeEach
    void setup() {
        timeslot = new TimeslotApp(LocalDateTime.parse("2022-12-08T10:15"),
                LocalDateTime.parse("2022-12-08T11:00"));
    }

    @Test
    void testConstructor() {
        assertThat(timeslot).isNotNull();
    }

    @Test
    void testGetStart() {
        assertThat(timeslot.getStart().toString()).isEqualTo("2022-12-08T10:15");
    }

    @Test
    void testGetEnd() {
        assertThat(timeslot.getEnd().toString()).isEqualTo("2022-12-08T11:00");
    }

    @Test
    void testEquals() {
        TimeslotApp timeslot1 = new TimeslotApp(LocalDateTime.parse("2022-12-08T10:15"),
                LocalDateTime.parse("2022-12-08T11:00"));
        TimeslotApp timeslot2 = new TimeslotApp(LocalDateTime.parse("2021-12-08T10:15"),
                LocalDateTime.parse("2021-12-08T11:00"));
        assertThat(timeslot1).isEqualTo(timeslot);
        assertThat(timeslot1).isNotEqualTo(timeslot2);
    }

    @Test
    void testHashCode() {
        TimeslotApp timeslot1 = new TimeslotApp(LocalDateTime.parse("2022-12-08T10:15"),
                LocalDateTime.parse("2022-12-08T11:00"));
        TimeslotApp timeslot2 = new TimeslotApp(LocalDateTime.parse("2021-12-08T10:15"),
                LocalDateTime.parse("2021-12-08T11:00"));
        assertThat(timeslot.hashCode()).isEqualTo(timeslot.hashCode());
        assertThat(timeslot1.hashCode()).isEqualTo(timeslot.hashCode());
        assertThat(timeslot1.hashCode()).isNotEqualTo(timeslot2.hashCode());
    }

    @Test
    void testToString() {
        assertThat(timeslot.toString()).isEqualTo("2022-12-08T10:15--2022-12-08T11:00");
    }

}