package nl.tudelft.sem.template.activities.domain;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ActivityTest {

    @Test
    void defaultConstructorTest() {
        Activity a = new Activity("owner@gmail.com", 1, null, 8, 10,
                null, LocalDateTime.of(2042, 12, 12, 20, 15),
                LocalDateTime.MAX, "8+", false, null);
        assertThat(a).isNotNull();
    }

    @Test
    void checkIfValidTrueTest() {
        Activity a = new Activity("owner@gmail.com", 1, null, 8, 10, null,
                LocalDateTime.of(2042, 12, 12, 20, 15),
                LocalDateTime.MAX, "8+", false, null);
        assertThat(a.checkIfValid()).isTrue();
    }

    @Test
    void checkIfValidRowersNullTest() {
        Activity a = new Activity("owner@gmail.com", null, null, null, null, null,
                LocalDateTime.of(2042, 12, 12, 20, 15),
                LocalDateTime.MAX, "8+", false, null);
        assertThat(a.checkIfValid()).isFalse();
    }

    @Test
    void checkIfValidInvalidCertificateTest() {
        Activity a = new Activity("owner@gmail.com", null, null, null, null, null,
                LocalDateTime.of(2042, 12, 12, 20, 15),
                LocalDateTime.MAX, "invalid", false, null);
        assertThat(a.checkIfValid()).isFalse();
    }

    @Test
    void checkIfValidInvalidCompetitionTrueInvalidGenderTest() {
        Activity a = new Activity("owner@gmail.com", null, null, null, null, null,
                LocalDateTime.of(2042, 12, 12, 20, 15),
                LocalDateTime.MAX, "8+", true, "Helicopter");
        assertThat(a.checkIfValid()).isFalse();
    }

    @Test
    void checkIfValidNullTimestampTest() {
        Activity a = new Activity("owner@gmail.com", 1, null, 8, 10, null,
                LocalDateTime.of(2042, 12, 12, 20, 15),
                null, "8+", false, null);
        assertThat(a.checkIfValid()).isFalse();
    }

    @Test
    void checkIfValidIncorrectTimestampTest() {
        Activity a = new Activity("owner@gmail.com", 1, null, 8, 10, null,
                LocalDateTime.of(2042, 12, 12, 20, 15),
                LocalDateTime.of(2042, 12, 12, 20, 14), "8+", false, null);
        assertThat(a.checkIfValid()).isFalse();
    }

    @Test
    void checkIfValidExpiredActivityTest() {
        Activity a = new Activity("owner@gmail.com", 1, null, 8, 10, null,
                LocalDateTime.of(2022, 12, 12, 20, 15),
                LocalDateTime.of(2022, 12, 12, 20, 20), "8+", false, null);
        assertThat(a.checkIfValid()).isFalse();
    }

    @Test
    void updateFieldsEmptyActivityNullFieldsTest() {
        Activity a = new Activity("owner@gmail.com", 1, null, 8, 10, null,
                LocalDateTime.of(2022, 12, 12, 20, 15),
                LocalDateTime.of(2022, 12, 12, 20, 20), "8+", false, null);
        Activity other = new Activity();
        a.setId(1L);
        other.setId(1L);
        a.updateFields(other);
        assertThat(a.getPositions().equals(new Positions(1, null, 8, 10, null))).isTrue();
        assertThat(a.getCertificate().equals("8+"));
    }

    @Test
    void updateFieldsEmptyActivityOneFieldTest() {
        Activity a = new Activity("owner@gmail.com", 1, null, 8, 10, null,
                LocalDateTime.of(2022, 12, 12, 20, 15),
                LocalDateTime.of(2022, 12, 12, 20, 20), "8+", false, null);
        Activity other = new Activity();
        other.setCertificate("4+");
        a.setId(1L);
        other.setId(1L);
        a.updateFields(other);
        assertThat(a.getPositions().equals(new Positions(1, null, 8, 10, null))).isTrue();
        assertThat(a.getCertificate().equals("4+"));
    }

    @Test
    void updateFieldsEmptyActivityOneObjectTest() {
        Activity a = new Activity("owner@gmail.com", 1, null, 8, 10, null,
                LocalDateTime.of(2022, 12, 12, 20, 15),
                LocalDateTime.of(2022, 12, 12, 20, 20), "8+", false, null);
        Activity other = new Activity();
        a.setPositions(new Positions(0, null, 7, 11, null));
        a.setId(1L);
        other.setId(1L);
        a.updateFields(other);
        assertThat(a.getPositions().equals(new Positions(0, null, 7, 11, null))).isTrue();
        assertThat(a.getCertificate().equals("8+"));
    }

    @Test
    void updateFieldsEmptyActivityAllFieldsTest() {
        Activity a = new Activity("owner@gmail.com", 1, null, 8, 10, null,
                LocalDateTime.of(2022, 12, 12, 20, 15),
                LocalDateTime.of(2022, 12, 12, 20, 20), "8+", false, null);
        Activity other = new Activity("owner@gmail.com", 0, null, 7, 11, null,
                LocalDateTime.of(2022, 12, 12, 20, 15),
                LocalDateTime.of(2022, 12, 12, 20, 20), "4+", false, null);
        a.setId(1L);
        other.setId(1L);
        a.updateFields(other);
        assertThat(a.getPositions().equals(new Positions(0, null, 7, 11, null))).isTrue();
        assertThat(a.getCertificate().equals("4+"));
    }

    @Test
    void checkNoArgsConstructorTest() {
        Activity a = new Activity();
        assertThat(a).isNotNull();
    }

    @Test
    void equalsTrueTest() {
        Activity a = new Activity("owner@gmail.com", 1, null, 8, 10, null,
                LocalDateTime.of(2022, 12, 12, 20, 15),
                LocalDateTime.of(2022, 12, 12, 20, 20), "8+", false, null);
        assertThat(a.equals(a)).isTrue();
    }

    @Test
    void equalsFalseTest() {
        Activity a = new Activity("owner@gmail.com", 1, null, 8, 10, null,
                LocalDateTime.of(2022, 12, 12, 20, 15),
                LocalDateTime.of(2022, 12, 12, 20, 20), "8+", false, null);
        Activity b = new Activity("different@gmail.com", 1, null, 8, 10, null,
                LocalDateTime.of(2022, 12, 12, 20, 15),
                LocalDateTime.of(2022, 12, 12, 20, 20), "8+", false, null);
        assertThat(a.equals(b)).isFalse();
    }

    @Test
    void hashCodeTestEqual() {
        Activity a = new Activity("owner@gmail.com", 1, null, 8, 10, null,
                LocalDateTime.of(2022, 12, 12, 20, 15),
                LocalDateTime.of(2022, 12, 12, 20, 20), "8+", false, null);
        Activity b = new Activity("owner@gmail.com", 1, null, 8, 10, null,
                LocalDateTime.of(2022, 12, 12, 20, 15),
                LocalDateTime.of(2022, 12, 12, 20, 20), "8+", false, null);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    void hashCodeTestNotEqual() {
        Activity a = new Activity("owner@gmail.com", 1, null, 8, 10, null,
                LocalDateTime.of(2022, 12, 12, 20, 15),
                LocalDateTime.of(2022, 12, 12, 20, 20), "8+", false, null);
        Activity b = new Activity("different@gmail.com", 1, null, 8, 10, null,
                LocalDateTime.of(2022, 12, 12, 20, 15),
                LocalDateTime.of(2022, 12, 12, 20, 20), "8+", false, null);
        assertThat(a.hashCode()).isNotEqualTo(b.hashCode());
    }
}