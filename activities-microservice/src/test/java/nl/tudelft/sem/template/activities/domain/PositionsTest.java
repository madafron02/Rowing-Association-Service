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
    void reduceByOneTestNone() {
        boolean result = positions.reduceByOne("none");
        assertThat(result).isFalse();
    }

    @Test
    void reduceByOneTestCoxFalse() {
        boolean result = positions.reduceByOne("cox");
        assertThat(positions.getCox()).isEqualTo(0);
        assertThat(result).isFalse();
    }

    @Test
    void reduceByOneTestCoxTrue() {
        positions.setCox(1);
        boolean result = positions.reduceByOne("cox");
        assertThat(positions.getCox()).isEqualTo(0);
        assertThat(result).isTrue();
    }

    @Test
    void reduceByOneTestCoachFalse() {
        positions.setCoach(0);
        boolean result = positions.reduceByOne("coach");
        assertThat(positions.getCoach()).isEqualTo(0);
        assertThat(result).isFalse();
    }

    @Test
    void reduceByOneTestCoachTrue() {
        assertThat(positions.getCoach()).isEqualTo(8);
        boolean result = positions.reduceByOne("coach");
        assertThat(positions.getCoach()).isEqualTo(7);
        assertThat(result).isTrue();
    }

    @Test
    void reduceByOneTestPortFalse() {
        boolean result = positions.reduceByOne("port");
        assertThat(positions.getPort()).isEqualTo(0);
        assertThat(result).isFalse();
    }

    @Test
    void reduceByOneTestPortTrue() {
        positions.setPort(2);
        boolean result = positions.reduceByOne("port");
        assertThat(positions.getPort()).isEqualTo(1);
        assertThat(result).isTrue();
    }

    @Test
    void reduceByOneTestStarboardFalse() {
        positions.setStarboard(0);
        boolean result = positions.reduceByOne("starboard");
        assertThat(positions.getStarboard()).isEqualTo(0);
        assertThat(result).isFalse();
    }

    @Test
    void reduceByOneTestStarboardTrue() {
        boolean result = positions.reduceByOne("starboard");
        assertThat(positions.getStarboard()).isEqualTo(2);
        assertThat(result).isTrue();
    }

    @Test
    void reduceByOneTestScullingFalse() {
        boolean result = positions.reduceByOne("sculling");
        assertThat(positions.getSculling()).isEqualTo(0);
        assertThat(result).isFalse();
    }

    @Test
    void reduceByOneTestScullingTrue() {
        positions.setSculling(1000);
        boolean result = positions.reduceByOne("sculling");
        assertThat(positions.getSculling()).isEqualTo(999);
        assertThat(result).isTrue();
    }

    @Test
    void checkIfValidFalseTest() {
        Positions empty = new Positions();
        assertThat(empty.checkIfValid()).isFalse();
    }

    @Test
    void checkIfValidCoxNotNullTest() {
        Positions empty = new Positions();
        empty.setCox(1);
        assertThat(empty.checkIfValid()).isTrue();
    }

    @Test
    void checkIfValidCoachNotNullTest() {
        Positions empty = new Positions();
        empty.setCoach(1);
        assertThat(empty.checkIfValid()).isTrue();
    }

    @Test
    void checkIfValidPortNotNullTest() {
        Positions empty = new Positions();
        empty.setPort(1);
        assertThat(empty.checkIfValid()).isTrue();
    }

    @Test
    void checkIfValidStarboardNotNullTest() {
        Positions empty = new Positions();
        empty.setStarboard(1);
        assertThat(empty.checkIfValid()).isTrue();
    }

    @Test
    void checkIfValidScullingNotNullTest() {
        Positions empty = new Positions();
        empty.setSculling(1);
        assertThat(empty.checkIfValid()).isTrue();
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
}