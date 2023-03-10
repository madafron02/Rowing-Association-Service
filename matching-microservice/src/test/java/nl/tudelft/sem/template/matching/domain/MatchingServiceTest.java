package nl.tudelft.sem.template.matching.domain;

import nl.tudelft.sem.template.matching.application.ActivityCommunication;
import nl.tudelft.sem.template.matching.application.Communication;
import nl.tudelft.sem.template.matching.application.NotificationCommunication;
import nl.tudelft.sem.template.matching.application.UsersCommunication;
import nl.tudelft.sem.template.matching.authentication.AuthManager;
import nl.tudelft.sem.template.matching.domain.database.CertificateRepo;
import nl.tudelft.sem.template.matching.domain.database.MatchingRepo;
import nl.tudelft.sem.template.matching.models.ActivityAvailabilityResponseModel;
import nl.tudelft.sem.template.matching.models.ActivityResponse;
import nl.tudelft.sem.template.matching.models.MatchingResponseModel;
import nl.tudelft.sem.template.matching.models.NotificationActivityModified;
import nl.tudelft.sem.template.matching.models.NotificationRequestModelOwner;
import nl.tudelft.sem.template.matching.models.NotificationRequestModelParticipant;
import nl.tudelft.sem.template.matching.models.UserPreferences;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MatchingServiceTest {

    private MatchingService service;

    private Sanitization sanitizationService;
    @Mock
    private AuthManager authManager;
    @Mock
    private static MatchingRepo matchingRepo;
    @Mock
    private UsersCommunication usersCommunication;
    @Mock
    private  NotificationCommunication notificationCommunication;
    @Mock
    private  ActivityCommunication activityCommunication;

    @Mock
    private  CertificateRepo certificateRepo;

    TimeslotApp timeslot;
    String position;
    UserApp user;
    Match match;


    @BeforeEach
    void setUp() {
        sanitizationService = new Sanitization(authManager, matchingRepo);
        matchingRepo.deleteAll();
        Communication communication = new Communication(usersCommunication,
                notificationCommunication,
                activityCommunication);
        service = new MatchingService(authManager, matchingRepo, communication, certificateRepo);
        timeslot = new TimeslotApp(LocalDateTime.now(),
                LocalDateTime.now().plusDays(1).plusHours(4));
        position = "cox";
        user = new UserApp("d.micloiu@icloud.com", "C4",
                "Female", "SEM", true);
        match = new Match("d.micloiu@tudelft.nl",
                2L,
                "l.tosa@tudelft.nl",
                "cox");
    }

    @AfterAll
    static void cleanUp() {
        matchingRepo.deleteAll();
    }

    @Test
    void filteringHandlerSetUp() {
        assertThat(service.filteringHandler).isNotNull();
    }

    @Test
    void submitAvailabilityNoFiltering() {
        when(authManager.getUserId()).thenReturn("d.micloiu@tudelft.nl");
        when(usersCommunication.getUserDetails("d.micloiu@tudelft.nl")).thenReturn(user);
        when(activityCommunication.getActivitiesByAvailability(timeslot))
                .thenReturn(new ActivityAvailabilityResponseModel(new ArrayList<>()));
        assertThat(service.submitAvailability(timeslot, position))
                .isInstanceOf(MatchingResponseModel.class);
    }

    @Test
    void filterActivities() {
        HashMap<String, Integer> positions = new HashMap<>();
        positions.put("cox", 2);

        ArrayList<ActivityApp> activities = new ArrayList<>();
        activities.add(null);
        activities.add(new ActivityApp(1L,
                "l.tosa@tudelft.nl",
                new TimeslotApp(LocalDateTime.now().plusMinutes(45),
                        LocalDateTime.now().plusHours(3)),
                null, null, positions, false, TypeOfActivity.TRAINING, "C4"));
        activities.add(new ActivityApp(2L,
                "l.tosa@tudelft.nl",
                new TimeslotApp(LocalDateTime.now(),
                        LocalDateTime.now().plusMinutes(45)),
                null, null, positions, false, TypeOfActivity.TRAINING, "C4"));

        activities.add(new ActivityApp(3L,
                "l.tosa@tudelft.nl",
                new TimeslotApp(LocalDateTime.now().plusDays(1),
                        LocalDateTime.now().plusDays(1).plusHours(1)),
                "Female", "SEM", positions, false, TypeOfActivity.COMPETITION, "4+"));

        activities.add(new ActivityApp(4L,
                "l.tosa@tudelft.nl",
                new TimeslotApp(LocalDateTime.now().plusHours(10),
                        LocalDateTime.now().plusDays(1)),
                "Female", "SEM", positions, false, TypeOfActivity.COMPETITION, "C4"));


        when(certificateRepo.getCertificateByName("C4")).thenReturn(Optional.of(new Certificate(1L, "C4+")));
        when(certificateRepo.getCertificateByName("4+")).thenReturn(Optional.of(new Certificate(2L, "4+")));
        // one because one of the activities is 30 min after the timeslot given by the user
        List<ActivityResponse> result = service.filterActivities(activities, new UserPreferences(timeslot, user, "cox"));
        assertThat(result.size()).isEqualTo(1);

        Match matchMade = new Match("d.micloiu@tudelft.nl",
                2L,
                "l.tosa@tudelft.nl",
                "cox");
        verify(matchingRepo, times(1)).save(matchMade);
    }

    @Test
    void filterActivityCompetitivenessFail() {
        HashMap<String, Integer> positions = new HashMap<>();
        positions.put("cox", 2);

        user = new UserApp("d.micloiu@icloud.com", "C4",
                "Male", "SEM", false);
        timeslot = new TimeslotApp(LocalDateTime.now(),
                LocalDateTime.now().plusDays(1).plusHours(4));

        ArrayList<ActivityApp> activities = new ArrayList<>();

        activities.add(new ActivityApp(4L,
                "l.tosa@tudelft.nl",
                new TimeslotApp(LocalDateTime.now().plusDays(1).plusHours(2),
                        LocalDateTime.now().plusDays(1).plusHours(3)),
                "Male", "SEM", positions, true, TypeOfActivity.COMPETITION, "C4"));


        when(certificateRepo.getCertificateByName("C4")).thenReturn(Optional.of(new Certificate(1L, "C4+")));
        List<ActivityResponse> result = service.filterActivities(activities, new UserPreferences(timeslot, user, "cox"));
        assertThat(result.size()).isEqualTo(0);

        verify(matchingRepo, times(0)).save(any());
    }

    @Test
    void filterActivityOrganisationPass() {
        HashMap<String, Integer> positions = new HashMap<>();
        positions.put("cox", 2);

        user = new UserApp("d.micloiu@icloud.com", "C4",
                "Male", "SEM", true);
        timeslot = new TimeslotApp(LocalDateTime.now(),
                LocalDateTime.now().plusDays(1).plusHours(4));

        ArrayList<ActivityApp> activities = new ArrayList<>();

        activities.add(new ActivityApp(4L,
                "l.tosa@tudelft.nl",
                new TimeslotApp(LocalDateTime.now().plusDays(1).plusHours(2),
                        LocalDateTime.now().plusDays(1).plusHours(3)),
                "Male", "SEM", positions, true, TypeOfActivity.COMPETITION, "C4"));


        when(certificateRepo.getCertificateByName("C4")).thenReturn(Optional.of(new Certificate(1L, "C4+")));
        List<ActivityResponse> result = service.filterActivities(activities, new UserPreferences(timeslot, user, "cox"));
        assertThat(result.size()).isEqualTo(1);


        Match matchMade = new Match("d.micloiu@tudelft.nl",
                2L,
                "l.tosa@tudelft.nl",
                "cox");
        verify(matchingRepo, times(1)).save(matchMade);
    }

    @Test
    void pickActivity() {
        when(matchingRepo.getMatchByMatchId(1L)).thenReturn(Optional.of(match));
        service.pickActivity(1L);

        assertThat(match.getStatus()).isEqualTo(Status.PENDING);
        verify(matchingRepo).save(match);

        NotificationRequestModelOwner emailOwner = new NotificationRequestModelOwner(match.getActivityInformation()
                .getOwnerId(),
                match.getParticipantId(),
                match.getActivityInformation().getActivityId(),
                activityCommunication.getActivityTimeslotById(match.getActivityInformation().getActivityId()));

        verify(notificationCommunication).sendReminderToOwner(emailOwner);
    }

    @Test
    void getPendingRequests() {
        match.setStatus(Status.PENDING);

        when(authManager.getUserId()).thenReturn("l.tosa@tudelft.nl");
        when(matchingRepo.getMatchesByActivityInformation_OwnerIdAndStatus("l.tosa@tudelft.nl",
                Status.PENDING))
                .thenReturn(List.of(match));
        assertThat(sanitizationService.getPendingRequests().size()).isEqualTo(1);

    }

    @Test
    void acceptOrDenyRequestMatchEmpty() {
        when(matchingRepo.getMatchByMatchId(1L)).thenReturn(Optional.empty());
        assertThat(service.acceptOrDenyRequest(1L, true)).isFalse();
    }

    @Test
    void acceptOrDenyRequestMaliciousOwner() {
        when(matchingRepo.getMatchByMatchId(1L)).thenReturn(Optional.of(match));
        when(authManager.getUserId()).thenReturn("m.user@tudelft.nl");
        assertThat(service.acceptOrDenyRequest(1L, true)).isFalse();
    }

    @Test
    void acceptOrDenyRequestMatchStatusNotPending() {
        match.setStatus(Status.ACCEPTED);
        when(authManager.getUserId()).thenReturn("l.tosa@tudelft.nl");
        when(matchingRepo.getMatchByMatchId(1L)).thenReturn(Optional.of(match));
        assertThat(service.acceptOrDenyRequest(1L, true)).isFalse();
    }

    @Test
    void acceptOrDenyRequestMatchDecisionTrue() {
        when(matchingRepo.getMatchByMatchId(1L)).thenReturn(Optional.of(match));
        when(authManager.getUserId()).thenReturn("l.tosa@tudelft.nl");
        match.setStatus(Status.PENDING);

        assertThat(service.acceptOrDenyRequest(1L, true)).isTrue();
        assertThat(match.getStatus()).isEqualTo(Status.ACCEPTED);
        verify(activityCommunication).updateActivity(2L, "cox");
        verify(matchingRepo).save(match);


        NotificationRequestModelParticipant emailParticipant = new NotificationRequestModelParticipant(match
                .getParticipantId(),
                match.getActivityInformation().getActivityId(),
                activityCommunication.getActivityTimeslotById(match.getActivityInformation().getActivityId()),
                true);

        verify(notificationCommunication).sendNotificationToParticipant(emailParticipant);

    }

    @Test
    void acceptOrDenyRequestMatchDecisionFalse() {
        when(matchingRepo.getMatchByMatchId(1L)).thenReturn(Optional.of(match));
        when(authManager.getUserId()).thenReturn("l.tosa@tudelft.nl");
        match.setStatus(Status.PENDING);

        assertThat(service.acceptOrDenyRequest(1L, false)).isTrue();
        assertThat(match.getStatus()).isEqualTo(Status.DECLINED);
        verify(matchingRepo).save(match);


        NotificationRequestModelParticipant emailParticipant = new NotificationRequestModelParticipant(match
                .getParticipantId(),
                match.getActivityInformation().getActivityId(),
                activityCommunication.getActivityTimeslotById(match.getActivityInformation().getActivityId()),
                false);

        verify(notificationCommunication).sendNotificationToParticipant(emailParticipant);
    }


    @Test
    void discardMatchesByActivity() {
        Match acceptedMatch = new Match("v.nitu@tudelft.nl",
                2L,
                "l.tosa@tudelft.nl",
                "starboard");
        acceptedMatch.setStatus(Status.ACCEPTED);

        when(matchingRepo.getMatchesByActivityInformation_ActivityId(2L)).thenReturn(List.of(match, acceptedMatch));
        service.discardMatchesByActivity(2L);

        NotificationActivityModified activityModifiedEmail = new NotificationActivityModified(acceptedMatch
                .getParticipantId(), 2L, null);
        verify(notificationCommunication).activityModifiedNotification(activityModifiedEmail);
        // two times since match and accepted match are not saved in db => same id
        verify(matchingRepo, times(2)).deleteById(match.getMatchId());
    }

    @Test
    void matchUserToActivityMutant() {
        HashMap<String, Integer> positions = new HashMap<>();
        positions.put("cox", 2);

        ArrayList<ActivityApp> activities = new ArrayList<>();
        activities.add(null);
        activities.add(new ActivityApp(1L,
                "l.tosa@tudelft.nl",
                new TimeslotApp(LocalDateTime.now().plusMinutes(45),
                        LocalDateTime.now().plusHours(3)),
                null, null, positions, false, TypeOfActivity.TRAINING, "C4"));
        activities.add(new ActivityApp(2L,
                "l.tosa@tudelft.nl",
                new TimeslotApp(LocalDateTime.now(),
                        LocalDateTime.now().plusMinutes(45)),
                null, null, positions, false, TypeOfActivity.TRAINING, "C4"));

        activities.add(new ActivityApp(3L,
                "l.tosa@tudelft.nl",
                new TimeslotApp(LocalDateTime.now().plusDays(1),
                        LocalDateTime.now().plusDays(1).plusHours(1)),
                "Female", "SEM", positions, false, TypeOfActivity.COMPETITION, "4+"));


        when(certificateRepo.getCertificateByName("C4")).thenReturn(Optional.of(new Certificate(1L, "C4")));
        when(certificateRepo.getCertificateByName("4+")).thenReturn(Optional.of(new Certificate(2L, "4+")));
        // one because one of the activities is 30 min after the timeslot given by the user
        List<ActivityResponse> result = service.filterActivities(activities, new UserPreferences(timeslot, user, "cox"));
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isNotNull();
    }
}