package nl.tudelft.sem.template.activities.domain;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ActivityTest {

    private final Positions validPositions = new Positions(1, null, 8, 10, null);

    private final Timeslot validTimeslot = new Timeslot(
            LocalDateTime.of(2042, 12, 12, 20, 15),
            LocalDateTime.of(2042, 12, 12, 20, 20));

    @Test
    void defaultConstructorTest() {
        Activity a = new Activity("owner@gmail.com", validPositions,
                validTimeslot, "8+", false, null, null);
        assertThat(a).isNotNull();
    }

    @Test
    void checkIfValidTrueTest() {
        Activity a = new Activity("owner@gmail.com", new Positions(1, 0, 8, 10, 0),
                validTimeslot, "8+", false, null, null);
        assertThat(a.checkIfValid()).isTrue();
    }

    @Test
    void checkIfValidRowersNullTest() {
        Activity a = new Activity("owner@gmail.com", null,
                validTimeslot, "8+", false, null, null);
        assertThat(a.checkIfValid()).isFalse();
    }

    @Test
    void checkIfValidInvalidCertificateTest() {
        Activity a = new Activity("owner@gmail.com", new Positions(1, 0, 8, 10, 0),
                validTimeslot, "invalid", false, null, null);
        assertThat(a.checkIfValid()).isFalse();
    }

    @Test
    void checkIfValidInvalidCompetitionTrueInvalidGenderTest() {
        Activity a = new Activity("owner@gmail.com",  null,
                validTimeslot, "8+", true, "Helicopter", "Laga");
        assertThat(a.checkIfValid()).isFalse();
    }

    @Test
    void checkIfValidNullTimeslotTest() {
        Timeslot endTimeNullTimeslot = new Timeslot(
                LocalDateTime.of(2042, 12, 12, 20, 15),
                null
        );
        Activity a = new Activity("owner@gmail.com", validPositions,
                endTimeNullTimeslot, "8+", false, null, null);
        assertThat(a.checkIfValid()).isFalse();
    }

    @Test
    void checkIfValidIncorrectTimeslotTest() {
        Timeslot endBeforeStartTimeslot = new Timeslot(
                LocalDateTime.of(2042, 12, 12, 20, 15),
                LocalDateTime.of(2042, 12, 12, 20, 14));
        Activity a = new Activity("owner@gmail.com", validPositions,
                endBeforeStartTimeslot, "8+", false, null, "Laga");
        assertThat(a.checkIfValid()).isFalse();
    }

    @Test
    void checkIfValidExpiredActivityTest() {
        Timeslot expiredTimeslot = new Timeslot(
                LocalDateTime.of(2022, 12, 12, 20, 15),
                LocalDateTime.of(2022, 12, 12, 20, 20));
        Activity a = new Activity("owner@gmail.com", validPositions,
                expiredTimeslot, "8+", false, null, null);
        assertThat(a.checkIfValid()).isFalse();
    }

    @Test
    void checkIfPositionsAndTimeslotValidTrueTest() {
        Activity a = new Activity("owner@gmail.com", new Positions(1, 0, 8, 10, 0),
                validTimeslot, "8+", false, null, null);
        assertThat(a.checkIfPositionsAndTimeslotValid()).isTrue();
    }

    @Test
    void checkIfPositionsAndTimeslotValidPositionsNullTest() {
        Activity a = new Activity("owner@gmail.com", validPositions,
                validTimeslot, "8+", false, null, null);
        a.setPositions(null);
        assertThat(a.checkIfPositionsAndTimeslotValid()).isFalse();
    }

    @Test
    void checkIfPositionsAndTimeslotValidPositionsInvalidTest() {
        Activity a = new Activity("owner@gmail.com", validPositions,
                validTimeslot, "8+", false, null, null);
        a.setPositions(new Positions());
        assertThat(a.checkIfPositionsAndTimeslotValid()).isFalse();
    }

    @Test
    void checkIfPositionsAndTimeslotValidTimeslotNullTest() {
        Activity a = new Activity("owner@gmail.com", validPositions,
                validTimeslot, "8+", false, null, null);
        a.setTimeslot(null);
        assertThat(a.checkIfPositionsAndTimeslotValid()).isFalse();
    }

    @Test
    void checkIfPositionsAndTimeslotValidTimeslotInvalidTest() {
        Activity a = new Activity("owner@gmail.com", validPositions,
                validTimeslot, "8+", false, null, null);
        a.getTimeslot().setEndTime(null);
        assertThat(a.checkIfPositionsAndTimeslotValid()).isFalse();
    }

    @Test
    void checkIfCompetitionValidNonCompetitionTest() {
        Activity a = new Activity("owner@gmail.com", validPositions,
                validTimeslot, "8+", false, null, null);
        assertThat(a.checkIfCompetitionValid()).isTrue();
    }

    @Test
    void checkIfCompetitionValidTrueTest() {
        Activity a = new Activity("owner@gmail.com", validPositions,
                validTimeslot, "8+", true, null, "Proteus");
        assertThat(a.checkIfCompetitionValid()).isTrue();
    }

    @Test
    void checkIfCompetitionValidOrganizationNullTest() {
        Activity a = new Activity("owner@gmail.com", validPositions,
                validTimeslot, "8+", true, null, null);
        assertThat(a.checkIfCompetitionValid()).isFalse();
    }

    @Test
    void checkIfCompetitionValidOrganizationInvalidGenderTest() {
        Activity a = new Activity("owner@gmail.com", validPositions,
                validTimeslot, "8+", true, "null", "Proteus");
        assertThat(a.checkIfCompetitionValid()).isFalse();
    }

    @Test
    void checkIfCompetitionValidOrganizationValidGenderTest() {
        Activity a = new Activity("owner@gmail.com", validPositions,
                validTimeslot, "8+", true, "Male", "Proteus");
        assertThat(a.checkIfCompetitionValid()).isTrue();
    }

    @Test
    void updateFieldsEmptyActivityNullFieldsTest() {
        Activity a = new Activity("owner@gmail.com", validPositions,
                validTimeslot, "8+", false, null, null);
        Activity other = new Activity();
        a.setId(1L);
        other.setId(1L);
        a.updateFields(other);
        Positions apos = a.getPositions();
        assertThat(apos.getCox().equals(validPositions.getCox())).isTrue();
        assertThat(apos.getCoach()).isNull();
        assertThat(apos.getPort().equals(validPositions.getPort())).isTrue();
        assertThat(apos.getStarboard().equals(validPositions.getStarboard())).isTrue();
        assertThat(apos.getSculling()).isNull();
        assertThat(a.getCertificate().equals("8+"));
    }

    @Test
    void updateFieldsEmptyActivityOneFieldTest() {
        Activity a = new Activity("owner@gmail.com", validPositions,
                validTimeslot, "8+", false, null, null);
        Activity other = new Activity();
        other.setCertificate("4+");
        a.setId(1L);
        other.setId(1L);
        a.updateFields(other);
        Positions apos = a.getPositions();
        assertThat(apos.getCox().equals(validPositions.getCox())).isTrue();
        assertThat(apos.getCoach()).isNull();
        assertThat(apos.getPort().equals(validPositions.getPort())).isTrue();
        assertThat(apos.getStarboard().equals(validPositions.getStarboard())).isTrue();
        assertThat(apos.getSculling()).isNull();
        assertThat(a.getCertificate().equals("4+"));
    }

    @Test
    void updateFieldsEmptyActivityOneObjectTest() {
        Activity a = new Activity("owner@gmail.com", validPositions,
                validTimeslot, "8+", false, null, null);
        Activity other = new Activity();
        a.setPositions(new Positions(0, null, 7, 11, null));
        a.setId(1L);
        other.setId(1L);
        a.updateFields(other);
        Positions apos = a.getPositions();
        assertThat(apos.getCox().equals(validPositions.getCox())).isFalse();
        assertThat(apos.getCoach()).isNull();
        assertThat(apos.getPort().equals(validPositions.getPort())).isFalse();
        assertThat(apos.getStarboard().equals(validPositions.getStarboard())).isFalse();
        assertThat(apos.getSculling()).isNull();
        assertThat(a.getCertificate().equals("8+"));
    }

    @Test
    void updateFieldsEmptyActivityAllFieldsTest() {
        Activity a = new Activity("owner@gmail.com", validPositions,
                validTimeslot, "8+", false, null, null);
        Activity other = new Activity("owner@gmail.com", new Positions(0, null, 7, 11, null),
                validTimeslot, "4+", false, null, null);
        a.setId(1L);
        other.setId(1L);
        a.updateFields(other);
        Positions apos = a.getPositions();
        Positions opos = other.getPositions();
        assertThat(apos.getCox().equals(opos.getCox())).isTrue();
        assertThat(apos.getCoach()).isNull();
        assertThat(apos.getPort().equals(opos.getPort())).isTrue();
        assertThat(apos.getStarboard().equals(opos.getStarboard())).isTrue();
        assertThat(apos.getSculling()).isNull();
        assertThat(a.getCertificate().equals("4+"));
    }

    @Test
    void checkNoArgsConstructorTest() {
        Activity a = new Activity();
        assertThat(a).isNotNull();
    }
}