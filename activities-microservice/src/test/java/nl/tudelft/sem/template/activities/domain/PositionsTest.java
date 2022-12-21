package nl.tudelft.sem.template.activities.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PositionsTest {

    private transient Positions positions;

    @BeforeEach
    void setUp() {
        positions = new Positions(null, 8, null, 3, 0);
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
    void getCoxCountTest() {
        assertThat(positions.getCoxCount()).isEqualTo(null);
    }

    @Test
    void getCoachCountTest() {
        assertThat(positions.getCoachCount()).isEqualTo(8);
    }

    @Test
    void getPortSideRowerCountTest() {
        assertThat(positions.getPortSideRowerCount()).isEqualTo(null);
    }

    @Test
    void getStarboardSideRowerCountTest() {
        assertThat(positions.getStarboardSideRowerCount()).isEqualTo(3);
    }

    @Test
    void getScullingRowerCountTest() {
        assertThat(positions.getScullingRowerCount()).isEqualTo(0);
    }

    @Test
    void setCoxCountTest() {
        positions.setCoxCount(2);
        assertThat(positions.getCoxCount()).isEqualTo(2);
    }

    @Test
    void setCoachCountTest() {
        positions.setCoachCount(2);
        assertThat(positions.getCoachCount()).isEqualTo(2);
    }

    @Test
    void setPortSideRowerCountTest() {
        positions.setPortSideRowerCount(2);
        assertThat(positions.getPortSideRowerCount()).isEqualTo(2);
    }

    @Test
    void setStarboardSideRowerCountTest() {
        positions.setStarboardSideRowerCount(2);
        assertThat(positions.getStarboardSideRowerCount()).isEqualTo(2);
    }

    @Test
    void setScullingRowerCountTest() {
        positions.setScullingRowerCount(2);
        assertThat(positions.getScullingRowerCount()).isEqualTo(2);
    }

    @Test
    void equalsTrue() {
        Positions other = new Positions(null, 8, null, 3, 0);
        assertThat(positions.equals(other)).isTrue();
    }

    @Test
    void equalsFalse() {
        Positions other = new Positions(null, 9, null, 3, 0);
        assertThat(positions.equals(other)).isFalse();
    }

    @Test
    void hashCodeEquals() {
        Positions other = new Positions(null, 8, null, 3, 0);
        assertThat(positions.hashCode()).isEqualTo(other.hashCode());
    }

    @Test
    void hashCodeNotEquals() {
        Positions other = new Positions(null, 9, null, 3, 0);
        assertThat(positions.hashCode()).isNotEqualTo(other.hashCode());
    }
}