package nl.tudelft.sem.template.matching.domain;

import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

class TypeOfActivityTest {

    @Test
    void values() {
        assertThat(TypeOfActivity.values())
                .isEqualTo(new TypeOfActivity[]{TypeOfActivity.TRAINING, TypeOfActivity.COMPETITION, TypeOfActivity.OTHER});
    }

    @Test
    void valueOf() {
        assertThat(TypeOfActivity.valueOf("TRAINING")).isEqualTo(TypeOfActivity.TRAINING);
    }
}