package nl.tudelft.sem.template.matching.domain.handlers;

import nl.tudelft.sem.template.matching.domain.ActivityApp;
import nl.tudelft.sem.template.matching.domain.MatchFilter;
import nl.tudelft.sem.template.matching.domain.TimeslotApp;
import nl.tudelft.sem.template.matching.domain.TypeOfActivity;
import nl.tudelft.sem.template.matching.domain.UserApp;
import nl.tudelft.sem.template.matching.models.UserPreferences;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class TimeConstraintHandlerTest {

    private FilteringHandler filteringHandler;
    private MatchFilter matchFilter;
    private ActivityApp activityApp;
    private UserApp user;

    @BeforeEach
    void setUp() {
        filteringHandler = new TimeConstraintHandler();
    }

    @Test
    void handleTrainingFalse() {
        HashMap<String, Integer> positions = new HashMap<>();
        positions.put("cox", 2);

        activityApp = new ActivityApp(3L,
                "l.tosa@tudelft.nl",
                new TimeslotApp(LocalDateTime.now(),
                        LocalDateTime.now().plusHours(2)),
                "Male", "SEM", positions, false,
                TypeOfActivity.TRAINING, "4+");
        user = new UserApp("d.micloiu@icloud.com", "C4",
                "Female", "SEM", true);
        matchFilter = new MatchFilter(activityApp, new UserPreferences(new TimeslotApp(LocalDateTime.now(),
                LocalDateTime.now().plusHours(12)), user, "cox"));

        assertThat(filteringHandler.handle(matchFilter)).isFalse();
    }

    @Test
    void handleTrainingBoundary() {
        HashMap<String, Integer> positions = new HashMap<>();
        positions.put("cox", 2);

        activityApp = new ActivityApp(3L,
                "l.tosa@tudelft.nl",
                new TimeslotApp(LocalDateTime.now().plusMinutes(30),
                        LocalDateTime.now().plusHours(2)),
                "Male", "SEM", positions, false,
                TypeOfActivity.TRAINING, "4+");
        user = new UserApp("d.micloiu@icloud.com", "C4",
                "Female", "SEM", true);
        matchFilter = new MatchFilter(activityApp, new UserPreferences(new TimeslotApp(LocalDateTime.now(),
                LocalDateTime.now().plusHours(12)), user, "cox"));

        assertThat(filteringHandler.handle(matchFilter)).isFalse();
    }

    @Test
    void handleTrainingTrue() {
        HashMap<String, Integer> positions = new HashMap<>();
        positions.put("cox", 2);

        activityApp = new ActivityApp(3L,
                "l.tosa@tudelft.nl",
                new TimeslotApp(LocalDateTime.now().plusMinutes(31),
                        LocalDateTime.now().plusHours(2)),
                "Male", "SEM", positions, false,
                TypeOfActivity.TRAINING, "4+");
        user = new UserApp("d.micloiu@icloud.com", "C4",
                "Female", "SEM", true);
        matchFilter = new MatchFilter(activityApp, new UserPreferences(new TimeslotApp(LocalDateTime.now(),
                LocalDateTime.now().plusHours(12)), user, "cox"));

        assertThat(filteringHandler.handle(matchFilter)).isTrue();

        // handle with next set
        filteringHandler.setNext(new GenderHandler());
        assertThat(filteringHandler.handle(matchFilter)).isFalse();
    }

    @Test
    void handleCompetitionFalse() {
        HashMap<String, Integer> positions = new HashMap<>();
        positions.put("cox", 2);

        activityApp = new ActivityApp(3L,
                "l.tosa@tudelft.nl",
                new TimeslotApp(LocalDateTime.now().plusDays(1),
                        LocalDateTime.now().plusDays(1).plusHours(2)),
                "Male", "SEM", positions, false,
                TypeOfActivity. COMPETITION, "4+");
        user = new UserApp("d.micloiu@icloud.com", "C4",
                "Female", "SEM", true);
        matchFilter = new MatchFilter(activityApp, new UserPreferences(new TimeslotApp(LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(12)), user, "cox"));

        assertThat(filteringHandler.handle(matchFilter)).isFalse();
    }

    @Test
    void handleCompetitionTrue() {
        HashMap<String, Integer> positions = new HashMap<>();
        positions.put("cox", 2);

        activityApp = new ActivityApp(3L,
                "l.tosa@tudelft.nl",
                new TimeslotApp(LocalDateTime.now().plusDays(1).plusMinutes(10),
                        LocalDateTime.now().plusDays(1).plusHours(2)),
                "Male", "SEM", positions, false,
                TypeOfActivity. COMPETITION, "4+");
        user = new UserApp("d.micloiu@icloud.com", "C4",
                "Female", "SEM", true);
        matchFilter = new MatchFilter(activityApp, new UserPreferences(new TimeslotApp(LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(12)), user, "cox"));

        assertThat(filteringHandler.handle(matchFilter)).isTrue();

        // handle with next set
        filteringHandler.setNext(new GenderHandler());
        assertThat(filteringHandler.handle(matchFilter)).isFalse();
    }


    @Test
    void handleTrainingMutant() {
        HashMap<String, Integer> positions = new HashMap<>();
        positions.put("cox", 2);

        activityApp = new ActivityApp(3L,
                "l.tosa@tudelft.nl",
                new TimeslotApp(LocalDateTime.now().plusMinutes(31),
                        LocalDateTime.now().plusHours(2)),
                null, null, positions, false,
                TypeOfActivity.TRAINING, "4+");
        user = new UserApp("d.micloiu@icloud.com", "C4",
                "Female", "SEM", true);
        matchFilter = new MatchFilter(activityApp, new UserPreferences(new TimeslotApp(LocalDateTime.now(),
                LocalDateTime.now().plusHours(12)), user, "cox"));

        assertThat(filteringHandler.handle(matchFilter)).isTrue();
    }

}