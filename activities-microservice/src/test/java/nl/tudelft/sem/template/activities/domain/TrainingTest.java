package nl.tudelft.sem.template.activities.domain;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TrainingTest {

    private transient Training training;

    private final transient String testEmail = "owner@gmail.com";

    private final transient Timeslot timeslot = new Timeslot(LocalDateTime.of(2042, 12, 12, 20, 15), LocalDateTime.MAX);

    @BeforeEach
    void setUp() {
        training = new Training(testEmail, 1, 0, 8, 10,
                0, timeslot, "8+");
    }

    @Test
    void defaultConstructor() {
        assertThat(training).isNotNull();
    }

    @Test
    void checkIfValidTrue() {
        assertThat(training.checkIfValid()).isTrue();
    }

    @Test
    void checkIfValidOwnerIdNull() {
        Training a = new Training(null, 1, 0, 8, 10, 0, timeslot, "8+");
        assertThat(a.checkIfValid()).isFalse();
    }

    @Test
    void checkIfValidInvalidCertificate() {
        Training a = new Training(testEmail, 0, 0, 0, 0, 0, timeslot, "invalid");
        assertThat(a.checkIfValid()).isFalse();
    }

    @Test
    void checkIfValidNullTimeslot() {
        Training a = new Training(testEmail, 1, 0, 8, 10, 0, null, "8+");
        assertThat(a.checkIfValid()).isFalse();
    }

    @Test
    void checkIfValidInvalidTimeslot() {
        Timeslot t = new Timeslot(LocalDateTime.of(2042, 12, 12, 20, 15), null);
        Training a = new Training(testEmail, 1, 0, 8, 10, 0, t, "8+");
        assertThat(a.checkIfValid()).isFalse();
    }

    @Test
    void checkIfValidIncorrectTimeslot() {
        Timeslot t = new Timeslot(LocalDateTime.of(2042, 12, 12, 20, 15),
                LocalDateTime.of(2042, 12, 12, 20, 14));
        Training a = new Training(testEmail, 1, 0, 8, 10, 0, t, "8+");
        assertThat(a.checkIfValid()).isFalse();
    }

    @Test
    void checkIfValidExpiredActivity() {
        Timeslot t = new Timeslot(LocalDateTime.of(2022, 12, 12, 20, 15),
                LocalDateTime.of(2022, 12, 12, 20, 20));
        Training a = new Training(testEmail, 1, 0, 8, 10, 0, t, "8+");
        assertThat(a.checkIfValid()).isFalse();
    }

    @Test
    void checkNoArgsConstructor() {
        Training a = new Training();
        assertThat(a).isNotNull();
    }

    @Test
    void equalsTrueSame() {
        assertThat(training.equals(training)).isTrue();
    }

    @Test
    void equalsTrue() {
        Training a = new Training(testEmail, 1, 0, 8, 10, 0, timeslot, "8+");
        assertThat(training.equals(a)).isTrue();
    }

    @Test
    void equalsFalseTestDifferentEmail() {
        Training b = new Training("different@gmail.com", 1, 0, 8, 10, 0, timeslot, "8+");
        assertThat(training.equals(b)).isFalse();
    }

    @Test
    void hashCodeTestEqual() {
        Training b = new Training(testEmail, 1, 0, 8, 10, 0, timeslot, "8+");
        assertThat(training.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    void hashCodeTestNotEqualDifferentEmail() {
        Training b = new Training("different@gmail.com", 1, 0, 8, 10, 0, timeslot, "8+");
        assertThat(training.hashCode()).isNotEqualTo(b.hashCode());
    }


    @Test
    void canEqualTrue() {
        Training a = new Training();
        assertThat(training.canEqual(a)).isTrue();
    }

    @Test
    void canEqualFalse() {
        String a = "";
        assertThat(training.canEqual(a)).isFalse();
    }

    @Test
    void getId() {
        assertThat(training.getId()).isNotNull();
    }

    @Test
    void getOwnerId() {
        assertThat(training.getOwnerId()).isEqualTo(testEmail);
    }

    @Test
    void getPositions() {
        assertThat(training.getPositions()).isNotNull();
    }

    @Test
    void getTimeslot() {
        assertThat(training.getTimeslot()).isNotNull();
    }

    @Test
    void getCertificate() {
        assertThat(training.getCertificate()).isEqualTo("8+");
    }

    @Test
    void setId() {
        training.setId(-1L);
        assertThat(training.getId()).isEqualTo(-1L);
    }

    @Test
    void setOwnerId() {
        training.setOwnerId("other");
        assertThat(training.getOwnerId()).isEqualTo("other");
    }

    @Test
    void setPositions() {
        Positions positions = new Positions(10, 8, 6, 5, 0);
        training.setPositions(positions);
        assertThat(training.getPositions()).isEqualTo(positions);
    }

    @Test
    void setTimeslot() {
        Timeslot t = new Timeslot();
        training.setTimeslot(t);
        assertThat(training.getTimeslot()).isEqualTo(t);
    }

    @Test
    void setCertificate() {
        training.setCertificate(null);
        assertThat(training.getCertificate()).isEqualTo(null);
    }
}