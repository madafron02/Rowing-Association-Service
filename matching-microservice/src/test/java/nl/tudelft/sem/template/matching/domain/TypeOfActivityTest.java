package nl.tudelft.sem.template.matching.domain;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TypeOfActivityTest {

    @Test
    void values() {
        assertThat(TypeOfActivity.values()).isEqualTo(new TypeOfActivity[]{TypeOfActivity.TRAINING, TypeOfActivity.COMPETITION});
    }

    @Test
    void valueOf() {
       assertThat(TypeOfActivity.valueOf("TRAINING")).isEqualTo(TypeOfActivity.TRAINING);
    }
}