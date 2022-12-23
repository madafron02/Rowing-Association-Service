package nl.tudelft.sem.template.matching.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MatchTest {

    private Match match;

    @BeforeEach
    void setUp() {
        match = new Match("d.micloiu@tudelft.nl", 1L,
                "l.tosa@tudelft.nl", "cox");
    }

    @Test
    void testConstructor() {
        assertThat(match).isNotNull();
    }

    @Test
    void testEmptyConstructor() {
        assertThat(new Match()).isNotNull();
    }

    @Test
    void testSetStatus() {
        match.setStatus(Status.PENDING);
        assertThat(match.getStatus()).isEqualTo(Status.PENDING);
    }

    @Test
    void testGetMatchId() {
        assertThat(match.getMatchId()).isEqualTo(0L);
    }

    @Test
    void testGetParticipantId() {
        assertThat(match.getParticipantId()).isEqualTo("d.micloiu@tudelft.nl");
    }

    @Test
    void testGetActivityId() {
        assertThat(match.getActivityId()).isEqualTo(1L);
    }

    @Test
    void testGetOwnerId() {
        assertThat(match.getOwnerId()).isEqualTo("l.tosa@tudelft.nl");
    }

    @Test
    void testGetStatus() {
        assertThat(match.getStatus()).isEqualTo(Status.MATCHED);
    }

    @Test
    void testGetPosition() {
        assertThat(match.getPosition()).isEqualTo("cox");
    }

    @Test
    void testEqualsSame() {
        assertThat(match.equals(match)).isTrue();
    }

    @Test
    void testEqualsDifferentButSameId() {
        Match newMatch = new Match("d.micloiu@tudelft.nl", 1L,
                "l.tosa@tudelft.nl", "starboard");
        assertThat(match.equals(newMatch)).isTrue();
    }

    @Test
    void testHashCode() {
        Match newMatch = new Match("d.micloiu@tudelft.nl", 1L,
                "l.tosa@tudelft.nl", "starboard");

        assertThat(match.hashCode() == newMatch.hashCode()).isTrue();
    }

    @Test
    void testEqualsDifferentObject() {
        TypeOfActivity type = TypeOfActivity.COMPETITION;
        assertThat(match.equals(type)).isFalse();
    }
}