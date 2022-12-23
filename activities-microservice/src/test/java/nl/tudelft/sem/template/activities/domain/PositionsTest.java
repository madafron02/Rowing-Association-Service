package nl.tudelft.sem.template.activities.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PositionsTest {

    private transient Positions positions;

    @BeforeEach
    void setUp() {
        positions = new Positions(0, 8, 0, 3, 0);
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
    void getPortTest() {
        assertThat(positions.getPort()).isEqualTo(0);
    }

    @Test
    void getStarboardTest() {
        assertThat(positions.getStarboard()).isEqualTo(3);
    }

    @Test
    void getScullingTest() {
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
    void setPortTest() {
        positions.setPort(2);
        assertThat(positions.getPort()).isEqualTo(2);
    }

    @Test
    void setStarboardTest() {
        positions.setStarboard(2);
        assertThat(positions.getStarboard()).isEqualTo(2);
    }

    @Test
    void setScullingTest() {
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