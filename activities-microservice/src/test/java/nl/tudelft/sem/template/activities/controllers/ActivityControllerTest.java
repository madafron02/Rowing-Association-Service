package nl.tudelft.sem.template.activities.controllers;

import nl.tudelft.sem.template.activities.authentication.AuthManager;
import nl.tudelft.sem.template.activities.domain.Activity;
import nl.tudelft.sem.template.activities.domain.ActivityRepository;
import nl.tudelft.sem.template.activities.domain.MatchingClient;
import nl.tudelft.sem.template.activities.domain.Positions;
import nl.tudelft.sem.template.activities.domain.Timeslot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ActivityControllerTest {

    private transient ActivityController activityController;

    private transient AuthManager authManager;

    private transient ActivityRepository activityRepository;

    private transient MatchingClient matchingClient;

    private transient MockMvc mockMvc;

    private final transient Timeslot ts1 = new Timeslot(
            LocalDateTime.of(2042, 12, 23, 10, 0),
            LocalDateTime.of(2042, 12, 23, 17, 15));

    private final transient Timeslot ts2 = new Timeslot(
            LocalDateTime.of(2042, 12, 23, 15, 0),
            LocalDateTime.of(2042, 12, 23, 20, 15));

    private transient Activity c1;

    private transient Activity c2;

    private transient Activity t1;

    private transient Activity t2;

    @BeforeEach
    void setUp() {
        authManager = Mockito.mock(AuthManager.class);
        activityRepository = Mockito.mock(ActivityRepository.class);
        matchingClient = Mockito.mock(MatchingClient.class);
        activityController = new ActivityController(authManager, activityRepository, matchingClient);
        this.mockMvc = MockMvcBuilders.standaloneSetup(activityController).build();
        c1 = new Activity("owner1@gmail.com", 1, 0, 4, 4, 0,
                ts1.getStartTime(), ts1.getEndTime(), "4+", false, "Male");
        c2 = new Activity("owner2@gmail.com", 0, 2, 0, 0, 8,
                ts1.getStartTime(), ts1.getEndTime(), "8+", false, "Female");
        t1 = new Activity("owner1@gmail.com", 1, 0, 4, 4, 0,
                ts2.getStartTime(), ts2.getEndTime(), "C4", true, "Male");
        t2 = new Activity("owner2@gmail.com", 1, 0, 4, 4, 0,
                ts1.getStartTime(), ts2.getEndTime(), "8+", true, "Female");
    }

    @Test
    public void constructorTest() {
        assertThat(activityController).isNotNull();
    }

    @Test
    public void getActivityTimeslotByIdNotFound() throws Exception {
        when(activityRepository.findById(1L)).thenReturn(Optional.empty());
        this.mockMvc
                .perform(get("/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getActivityTimeslotByIdOkIntegration() throws Exception {
        when(activityRepository.findById(1L)).thenReturn(Optional.of(c1));
        this.mockMvc
                .perform(get("/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getActivityTimeslotByIdNotFoundIntegration() throws Exception {
        when(activityRepository.findById(1L)).thenReturn(Optional.empty());
        this.mockMvc
                .perform(get("/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getActivityTimeslotByIdOk() throws Exception {
        when(activityRepository.findById(1L)).thenReturn(Optional.of(c1));
        Timeslot timeslot = activityController.getActivityTimeslotById(1L).getBody();
        assertThat(timeslot).isEqualTo(c1.getTimeslot());
    }

    @Test
    void getAllActivitiesWithinTimeslot() {
        List<Activity> list = List.of(t1, t2, c2);
        when(activityRepository.findActivitiesByTimeslot(ts1.getStartTime(), ts1.getEndTime())).thenReturn(list);
        List<Activity> trainings = activityController.getAllActivitiesWithinTimeslot(ts1).getBody().getActivities();
        assertThat(trainings.size()).isEqualTo(3);
    }

    @Test
    void getAllActivitiesByOwner() {
        List<Activity> list = List.of(t1, c1);
        when(activityRepository.findActivitiesByOwnerId("owner1@gmail.com")).thenReturn(list);
        when(authManager.getUserId()).thenReturn("owner1@gmail.com");
        List<Activity> trainings = activityController.getAllActivitiesByOwner().getBody().getActivities();
        assertThat(trainings.size() == 2).isTrue();
    }

    @Test
    void createActivity() {
        when(matchingClient.validateCertificate(t1.getCertificate())).thenReturn(true);
        when(authManager.getUserId()).thenReturn(t1.getOwnerId());
        String response = activityController.createActivity(t1).getBody();
        assertThat(response).isEqualTo("Activity created successfully!");
        verify(activityRepository).save(t1);
    }

    @Test
    void createActivityInvalid() {
        t1.setPositions(new Positions());
        when(matchingClient.validateCertificate(t1.getCertificate())).thenReturn(false);
        when(authManager.getUserId()).thenReturn(t1.getOwnerId());
        activityController.createActivity(t1).getBody();
        verify(activityRepository, never()).save(any());
    }

    @Test
    void deleteActivity() {
        when(activityRepository.findById(1L)).thenReturn(Optional.of(c1));
        when(authManager.getUserId()).thenReturn(c1.getOwnerId());
        String response = activityController.deleteActivity(1L).getBody();
        assertThat(response).isEqualTo("Activity with the id 1 has been deleted successfully!");
        verify(activityRepository).delete(c1);
        verify(matchingClient).deleteAllMatches(anyLong());
    }

    @Test
    void deleteActivityNullId() {
        activityController.deleteActivity(null).getBody();
        verify(activityRepository, never()).delete(c1);
        verify(matchingClient, never()).deleteAllMatches(anyLong());
    }

    @Test
    void deleteActivityNotFound() {
        when(activityRepository.findById(1L)).thenReturn(Optional.empty());
        activityController.deleteActivity(1L).getBody();
        verify(activityRepository, never()).delete(c1);
        verify(matchingClient, never()).deleteAllMatches(anyLong());
    }

    @Test
    void deleteActivityIdMismatch() {
        when(activityRepository.findById(1L)).thenReturn(Optional.of(c1));
        when(authManager.getUserId()).thenReturn("an ID that does not match");
        activityController.deleteActivity(1L).getBody();
        verify(activityRepository, never()).delete(c1);
        verify(matchingClient, never()).deleteAllMatches(anyLong());
    }

    @Test
    void updateActivity() {
        Activity request = new Activity();
        request.setId(1L);
        request.setGender("Female");
        when(activityRepository.findById(1L)).thenReturn(Optional.of(c1));
        when(authManager.getUserId()).thenReturn(c1.getOwnerId());
        when(matchingClient.validateCertificate(any())).thenReturn(true);
        activityController.updateActivity(request).toString();
        verify(activityRepository).save(any());
        verify(matchingClient).deleteAllMatches(anyLong());
    }

    @Test
    void updateActivityNotFound() {
        Activity request = new Activity();
        request.setId(1L);
        request.setGender("Female");
        when(activityRepository.findById(1L)).thenReturn(Optional.empty());
        when(authManager.getUserId()).thenReturn(c1.getOwnerId());
        when(matchingClient.validateCertificate(any())).thenReturn(true);
        activityController.updateActivity(request);
        verify(activityRepository, never()).save(any());
        verify(matchingClient, never()).deleteAllMatches(anyLong());
    }

    @Test
    void updateActivityiInvalid() {
        when(activityRepository.findById(1L)).thenReturn(Optional.empty());
        when(authManager.getUserId()).thenReturn(c1.getOwnerId());
        when(matchingClient.validateCertificate(any())).thenReturn(false);
        Activity request = new Activity();
        activityController.updateActivity(request);
        verify(activityRepository, never()).save(any());
        verify(matchingClient, never()).deleteAllMatches(anyLong());
    }

    @Test
    void updateActivityiInvalidIdMismatch() {
        when(activityRepository.findById(1L)).thenReturn(Optional.empty());
        when(authManager.getUserId()).thenReturn("a mismatching id");
        when(matchingClient.validateCertificate(any())).thenReturn(false);
        Activity request = new Activity();
        activityController.updateActivity(request);
        verify(activityRepository, never()).save(any());
        verify(matchingClient, never()).deleteAllMatches(anyLong());
    }

    @Test
    void reduceByOneTest() {
        when(activityRepository.findById(1L)).thenReturn(Optional.of(t1));
        String name = "cox";
        Activity training =  activityController.reduceByOne(1L, name).getBody();
        assertThat(t1.getPositions().getCox()).isEqualTo(0);
        verify(activityRepository).save(t1);
    }

    @Test
    void reduceByOneTestIntegration() {
        when(activityRepository.findById(1L)).thenReturn(Optional.of(t1));
        String name = "cox";
        Activity training =  activityController.reduceByOne(1L, name).getBody();
        assertThat(t1.getPositions().getCox()).isEqualTo(0);
        verify(activityRepository).save(t1);
    }

    @Test
    void reduceByOneTestButZero() {
        when(activityRepository.findById(1L)).thenReturn(Optional.of(t1));
        String name = "coach";
        Activity training =  activityController.reduceByOne(1L, name).getBody();
        assertThat(t1.getPositions().getCoach()).isEqualTo(0);
        verify(activityRepository, never()).save(any());
    }

    @Test
    void reduceByOneTestButNotFound() {
        when(activityRepository.findById(1L)).thenReturn(Optional.empty());
        String name = "coach";
        Activity training =  activityController.reduceByOne(1L, name).getBody();
        assertThat(t1.getPositions().getCoach()).isEqualTo(0);
        verify(activityRepository, never()).save(any());
    }
}