package nl.tudelft.sem.template.matching.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ActivityAppTest {

    private HashMap<String, Integer> positions = new HashMap<>();

    @BeforeEach
    void setUp() {
        positions.put("cox", 2);
    }

    @Test
    void setTypeOfActivityTraining() {
        ActivityApp activityApp = new ActivityApp(1L,
                "l.tosa@tudelft.nl",
                new TimeslotApp(LocalDateTime.now().plusMinutes(45),
                        LocalDateTime.now().plusHours(3)),
                null, null, positions, false, null, "C4");

        assertThat(activityApp.getType()).isNull();

        activityApp.setTypeOfActivity();
        assertThat(activityApp.getType()).isEqualTo(TypeOfActivity.TRAINING);
    }

    @Test
    void setTypeOfActivityCompetition() {
        ActivityApp activityApp = new ActivityApp(1L,
                "l.tosa@tudelft.nl",
                new TimeslotApp(LocalDateTime.now().plusMinutes(45),
                        LocalDateTime.now().plusHours(3)),
                "Male", "SEM", positions, false, null, "C4");

        assertThat(activityApp.getType()).isNull();

        activityApp.setTypeOfActivity();
        assertThat(activityApp.getType()).isEqualTo(TypeOfActivity.COMPETITION);
    }

    @Test
    void setTypeOfActivitySomethingWrongGender() {
        ActivityApp activityApp = new ActivityApp(1L,
                "l.tosa@tudelft.nl",
                new TimeslotApp(LocalDateTime.now().plusMinutes(45),
                        LocalDateTime.now().plusHours(3)),
                "Male", null, positions, false, null, "C4");

        assertThat(activityApp.getType()).isNull();

        assertThat(activityApp.setTypeOfActivity()).isNull();
    }

    @Test
    void setTypeOfActivitySomethingWrongOrganisation() {
        ActivityApp activityApp = new ActivityApp(1L,
                "l.tosa@tudelft.nl",
                new TimeslotApp(LocalDateTime.now().plusMinutes(45),
                        LocalDateTime.now().plusHours(3)),
                null, "SEM", positions, false, null, "C4");

        assertThat(activityApp.getType()).isNull();

        assertThat(activityApp.setTypeOfActivity()).isNull();
    }
}