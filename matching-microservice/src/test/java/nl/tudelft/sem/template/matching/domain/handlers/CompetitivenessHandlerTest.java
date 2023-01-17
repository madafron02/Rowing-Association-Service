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

class CompetitivenessHandlerTest {

    private FilteringHandler filteringHandler;
    private MatchFilter matchFilter;
    private ActivityApp activityApp;
    private UserApp user;

    @BeforeEach
    void setUp() {
        filteringHandler = new CompetitivenessHandler();
        HashMap<String, Integer> positions = new HashMap<>();
        positions.put("cox", 2);

        activityApp = new ActivityApp(3L,
                "l.tosa@tudelft.nl",
                new TimeslotApp(LocalDateTime.parse("2022-12-08T13:15"),
                        LocalDateTime.parse("2022-12-08T14:00")),
                "Male", "SEM", positions, false,
                TypeOfActivity.COMPETITION, "4+");
        user = new UserApp("d.micloiu@icloud.com", "C4",
                "Female", "SEM", true);

        matchFilter = new MatchFilter(activityApp, new UserPreferences(
                new TimeslotApp(LocalDateTime.parse("2022-12-08T10:15"),
                LocalDateTime.parse("2022-12-08T17:00")), user, "coach"));
    }

    @Test
    void handleTrue() {
        // competitive user for uncompetitive activity
        assertThat(filteringHandler.handle(matchFilter)).isTrue();

        // competitive user for competitive activity
        activityApp.getProperties().setCompetition(true);
        assertThat(filteringHandler.handle(matchFilter)).isTrue();

        // uncompetitive user for uncompetitive activity
        activityApp.getProperties().setCompetition(false);
        user.setCompetitive(false);
        assertThat(filteringHandler.handle(matchFilter)).isTrue();

        filteringHandler.setNext(new GenderHandler());
        assertThat(filteringHandler.handle(matchFilter)).isFalse();
    }

    @Test
    void handleFalse() {
        // uncompetitive user for competitive activity
        user.setCompetitive(false);
        activityApp.getProperties().setCompetition(true);
        assertThat(filteringHandler.handle(matchFilter)).isFalse();
    }
}