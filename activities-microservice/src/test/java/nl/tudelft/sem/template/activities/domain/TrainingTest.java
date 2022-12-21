package nl.tudelft.sem.template.activities.domain;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TrainingTest {

    private transient String testEmail = "owner@gmail.com";

    private transient Timeslot timeslot = new Timeslot(LocalDateTime.of(2042, 12, 12, 20, 15), LocalDateTime.MAX);

    @Test
    void defaultConstructorTest() {
        Training a = new Training(testEmail, 1, 0, 8, 10,
                0, timeslot, "8+");
        assertThat(a).isNotNull();
    }

    @Test
    void checkIfValidTrueTest() {
        Training a = new Training(testEmail, 1, 0, 8, 10, 0, timeslot, "8+");
        assertThat(a.checkIfValid()).isTrue();
    }

    @Test
    void checkIfValidOwnerIdNullTest() {
        Training a = new Training(null, 1, 0, 8, 10, 0, timeslot, "8+");
        assertThat(a.checkIfValid()).isFalse();
    }

    @Test
    void checkIfValidInvalidCertificateTest() {
        Training a = new Training(testEmail, 0, 0, 0, 0, 0, timeslot, "invalid");
        assertThat(a.checkIfValid()).isFalse();
    }

    @Test
    void checkIfValidNullTimeslotTest() {
        Training a = new Training(testEmail, 1, 0, 8, 10, 0, null, "8+");
        assertThat(a.checkIfValid()).isFalse();
    }

    @Test
    void checkIfValidInvalidTimeslotTest() {
        Timeslot t = new Timeslot(LocalDateTime.of(2042, 12, 12, 20, 15), null);
        Training a = new Training(testEmail, 1, 0, 8, 10, 0, t, "8+");
        assertThat(a.checkIfValid()).isFalse();
    }

    @Test
    void checkIfValidIncorrectTimeslotTest() {
        Timeslot t = new Timeslot(LocalDateTime.of(2042, 12, 12, 20, 15),
                LocalDateTime.of(2042, 12, 12, 20, 14));
        Training a = new Training(testEmail, 1, 0, 8, 10, 0, t, "8+");
        assertThat(a.checkIfValid()).isFalse();
    }

    @Test
    void checkIfValidExpiredActivityTest() {
        Timeslot t = new Timeslot(LocalDateTime.of(2022, 12, 12, 20, 15),
                LocalDateTime.of(2022, 12, 12, 20, 20));
        Training a = new Training(testEmail, 1, 0, 8, 10, 0, t, "8+");
        assertThat(a.checkIfValid()).isFalse();
    }

    @Test
    void checkNoArgsConstructorTest() {
        Training a = new Training();
        assertThat(a).isNotNull();
    }

    @Test
    void equalsTrueTest() {
        Training a = new Training(testEmail, 1, 0, 8, 10, 0, timeslot, "8+");
        assertThat(a.equals(a)).isTrue();
    }

    @Test
    void equalsFalseTest() {
        Training a = new Training(testEmail, 1, 0, 8, 10, 0, timeslot, "8+");
        Training b = new Training("different@gmail.com", 1, 0, 8, 10, 0, timeslot, "8+");
        assertThat(a.equals(b)).isFalse();
    }

    @Test
    void hashCodeTestEqual() {
        Training a = new Training(testEmail, 1, 0, 8, 10, 0, timeslot, "8+");
        Training b = new Training(testEmail, 1, 0, 8, 10, 0, timeslot, "8+");
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    void hashCodeTestNotEqual() {
        Training a = new Training(testEmail, 1, 0, 8, 10, 0, timeslot, "8+");
        Training b = new Training("different@gmail.com", 1, 0, 8, 10, 0, timeslot, "8+");
        assertThat(a.hashCode()).isNotEqualTo(b.hashCode());
    }
}