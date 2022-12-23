package nl.tudelft.sem.template.matching.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class TimeslotAppConverterTest {

    private TimeslotAppConverter converter;
    private TimeslotApp timeslot;

    @BeforeEach
    void setUp() {
        timeslot = new TimeslotApp(LocalDateTime.parse("2022-12-08T10:15"),
                LocalDateTime.parse("2022-12-08T11:00"));
        converter = new TimeslotAppConverter();
    }

    @Test
    void convertToDatabaseColumn() {
        assertThat(converter.convertToDatabaseColumn(timeslot))
                .isEqualTo("2022-12-08T10:15--2022-12-08T11:00");
    }

    @Test
    void convertToEntityAttribute() {
        assertThat(converter.convertToEntityAttribute("2022-12-08T10:15--2022-12-08T11:00"))
                .isEqualTo(timeslot);
    }
}