package nl.tudelft.sem.template.matching.domain.handlers;

import nl.tudelft.sem.template.matching.domain.ActivityApp;
import nl.tudelft.sem.template.matching.domain.Certificate;
import nl.tudelft.sem.template.matching.domain.MatchFilter;
import nl.tudelft.sem.template.matching.domain.TimeslotApp;
import nl.tudelft.sem.template.matching.domain.TypeOfActivity;
import nl.tudelft.sem.template.matching.domain.UserApp;
import nl.tudelft.sem.template.matching.domain.database.CertificateRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CertificateHandlerTest {

    private FilteringHandler filteringHandler;
    private ActivityApp activityApp;

    @Mock
    private CertificateRepo certificateRepo;

    @BeforeEach
    void setUp() {
        filteringHandler = new CertificateHandler(certificateRepo);
    }

    @Test
    void handleNotCox() {
        HashMap<String, Integer> positions = new HashMap<>();
        positions.put("cox", 2);

        activityApp = new ActivityApp(3L,
                "l.tosa@tudelft.nl",
                new TimeslotApp(LocalDateTime.parse("2022-12-08T13:15"),
                        LocalDateTime.parse("2022-12-08T14:00")),
                "Male", "SEM", positions, false,
                TypeOfActivity.COMPETITION, "4+");

        UserApp user = new UserApp("d.micloiu@icloud.com", "C4",
                "Female", "SEM", true);

        MatchFilter matchFilter = new MatchFilter(activityApp, user, "coach",
                new TimeslotApp(LocalDateTime.parse("2022-12-08T10:15"),
                        LocalDateTime.parse("2022-12-08T17:00")));

        assertThat(filteringHandler.handle(matchFilter)).isTrue();

        filteringHandler.setNext(new GenderHandler());
        assertThat(filteringHandler.handle(matchFilter)).isFalse();

    }

    @Test
    void handleWithoutCox() {
        HashMap<String, Integer> positions = new HashMap<>();
        positions.put("cox", 2);

        activityApp = new ActivityApp(3L,
                "l.tosa@tudelft.nl",
                new TimeslotApp(LocalDateTime.parse("2022-12-08T13:15"),
                        LocalDateTime.parse("2022-12-08T14:00")),
                "Male", "SEM", positions, false,
                TypeOfActivity.COMPETITION, "C4");

        UserApp user = new UserApp("d.micloiu@icloud.com", "4+",
                "Female", "SEM", true);

        MatchFilter matchFilter = new MatchFilter(activityApp, user, "cox",
                new TimeslotApp(LocalDateTime.parse("2022-12-08T10:15"),
                        LocalDateTime.parse("2022-12-08T17:00")));

        when(certificateRepo.getCertificateByName("C4")).thenReturn(Optional.of(new Certificate(1L, "C4+")));
        when(certificateRepo.getCertificateByName("4+")).thenReturn(Optional.of(new Certificate(2L, "4+")));

        // user can participate since C4 < 4+
        assertThat(filteringHandler.handle(matchFilter)).isTrue();

        filteringHandler.setNext(new GenderHandler());
        assertThat(filteringHandler.handle(matchFilter)).isFalse();

        // swap so that user is not able to participate
        user.setCertificate("C4");
        activityApp.getProperties().setCertificate("4+");
        assertThat(filteringHandler.handle(matchFilter)).isFalse();
    }
}