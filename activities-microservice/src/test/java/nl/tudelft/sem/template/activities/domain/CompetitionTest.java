package nl.tudelft.sem.template.activities.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CompetitionTest {

    private transient Competition competition;

    private transient Timeslot timeslot = new Timeslot(LocalDateTime.of(2042, 12, 12, 20, 15), LocalDateTime.MAX);

    @BeforeEach
    void setUp() {
        competition = new Competition("owner@gmail.com", 1, 0, 8, 10,
                0, timeslot, "8+", "Male", false, "Proteus");
    }

    @Test
    void checkIfValidGenderNull() {
        competition.setGender(null);
        assertThat(competition.checkIfValid()).isFalse();
    }

    @Test
    void checkIfValidInvalidGender() {
        competition.setGender("INVALID_GENDER");
        assertThat(competition.checkIfValid()).isFalse();
    }

    @Test
    void checkIfValidNullOrganisation() {
        competition.setOrganisation(null);
        assertThat(competition.checkIfValid()).isFalse();
    }

    @Test
    void checkIfValidTrue() {
        assertThat(competition.checkIfValid()).isTrue();
    }

    @Test
    void testEqualsSame() {
        assertThat(competition.equals(competition)).isTrue();
    }

    @Test
    void testNotEqualsParentOwnerId() {
        Competition other = new Competition("other@gmail.com", 1, 0, 8, 10,
                0, timeslot, "8+", "Male", false, "Proteus");
        assertThat(competition.equals(other)).isFalse();
    }

    @Test
    void testNotEqualsOrganisation() {
        Competition other = new Competition("owner@gmail.com", 1, 0, 8, 10,
                0, timeslot, "8+", "Male", false, "Laga");
        assertThat(competition.equals(other)).isFalse();
    }

    @Test
    void canEqualTrue() {
        Competition c = new Competition();
        assertThat(competition.canEqual(c)).isTrue();
    }

    @Test
    void canEqualFalse() {
        Training c = new Training();
        assertThat(competition.canEqual(c)).isFalse();
    }

    @Test
    void testHashCodeTwoObjectsEqual() {
        Competition other = new Competition("owner@gmail.com", 1, 0, 8, 10,
                0, timeslot, "8+", "Male", false, "Proteus");
        assertThat(competition.hashCode()).isEqualTo(other.hashCode());
    }

    @Test
    void testHashCodeTwoObjectsDifferentOrganisations() {
        Competition other = new Competition("owner@gmail.com", 1, 0, 8, 10,
                0, timeslot, "8+", "Male", false, "Laga");
        assertThat(competition.hashCode()).isNotEqualTo(other.hashCode());
    }

    @Test
    void getGender() {
        assertThat(competition.getGender()).isEqualTo("Male");
    }

    @Test
    void isCompetitiveness() {
        assertThat(competition.isCompetitiveness()).isEqualTo(false);
    }

    @Test
    void getOrganisation() {
        assertThat(competition.getOrganisation()).isEqualTo("Proteus");
    }

    @Test
    void setGender() {
        competition.setGender("Female");
        assertThat(competition.getGender()).isEqualTo("Female");
    }

    @Test
    void setCompetitiveness() {
        competition.setCompetitiveness(true);
        assertThat(competition.isCompetitiveness()).isEqualTo(true);
    }

    @Test
    void setOrganisation() {
        competition.setOrganisation("Laga");
        assertThat(competition.getOrganisation()).isEqualTo("Laga");
    }
}