package nl.tudelft.sem.template.activities.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PositionsTest {

    private Positions positions;

    @BeforeEach
    void setUp() {
        positions = new Positions(0, 8, 0, 3, 0);
    }

    @Test
    void allArgsConstructorTest() {
        assertThat(positions).isNotNull();
    }

    @Test
    void reduceByOneTestCox() {
        boolean result = positions.reduceByOne("cox");
        assertThat(positions.getCox()).isEqualTo(0);
        assertThat(result).isFalse();
    }

    @Test
    void reduceByOneTestCoach() {
        assertThat(positions.getCoach()).isEqualTo(8);
        boolean result = positions.reduceByOne("coach");
        assertThat(positions.getCoach()).isEqualTo(7);
        assertThat(result).isTrue();
    }

    @Test
    void reduceByOneTestPort() {
        boolean result = positions.reduceByOne("port");
        assertThat(positions.getPort()).isEqualTo(0);
        assertThat(result).isFalse();
    }

    @Test
    void reduceByOneTestStarboard() {
        boolean result = positions.reduceByOne("starboard");
        assertThat(positions.getStarboard()).isEqualTo(2);
        assertThat(result).isTrue();
    }

    @Test
    void reduceByOneTestSculling() {
        boolean result = positions.reduceByOne("sculling");
        assertThat(positions.getSculling()).isEqualTo(0);
        assertThat(result).isFalse();
    }

    @Test
    void emptyConstructorTest() {
        Positions empty = new Positions();
        assertThat(empty).isNotNull();
    }

    @Test
    void constructorTest() {
        assertThat(positions).isNotNull();
    }

    @Test
    void getCoxTest() {
        assertThat(positions.getCox()).isEqualTo(0);
    }

    @Test
    void getCoachTest() {
        assertThat(positions.getCoach()).isEqualTo(8);
    }

    @Test
    void getPortSideRowerTest() {
        assertThat(positions.getPort()).isEqualTo(0);
    }

    @Test
    void getStarboardSideRowerTest() {
        assertThat(positions.getStarboard()).isEqualTo(3);
    }

    @Test
    void getScullingRowerTest() {
        assertThat(positions.getSculling()).isEqualTo(0);
    }

    @Test
    void setCoxTest() {
        positions.setCox(2);
        assertThat(positions.getCox()).isEqualTo(2);
    }

    @Test
    void setCoachTest() {
        positions.setCoach(2);
        assertThat(positions.getCoach()).isEqualTo(2);
    }

    @Test
    void setPortSideRowerTest() {
        positions.setPort(2);
        assertThat(positions.getPort()).isEqualTo(2);
    }

    @Test
    void setStarboardSideRowerTest() {
        positions.setStarboard(2);
        assertThat(positions.getStarboard()).isEqualTo(2);
    }

    @Test
    void setScullingRowerTest() {
        positions.setSculling(2);
        assertThat(positions.getSculling()).isEqualTo(2);
    }

    @Test
    void equalsTrue() {
        Positions other = new Positions(0, 8, 0, 3, 0);
        assertThat(positions.equals(other)).isTrue();
    }

    @Test
    void equalsFalse() {
        Positions other = new Positions(0, 9, 0, 3, 0);
        assertThat(positions.equals(other)).isFalse();
    }

    @Test
    void hashCodeEquals() {
        Positions other = new Positions(0, 8, 0, 3, 0);
        assertThat(positions.hashCode()).isEqualTo(other.hashCode());
    }

    @Test
    void hashCodeNotEquals() {
        Positions other = new Positions(0, 9, 0, 3, 0);
        assertThat(positions.hashCode()).isNotEqualTo(other.hashCode());
    }
}