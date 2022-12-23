package nl.tudelft.sem.template.activities.controllers;

import nl.tudelft.sem.template.activities.authentication.AuthManager;
import nl.tudelft.sem.template.activities.domain.Competition;
import nl.tudelft.sem.template.activities.domain.MatchingClient;
import nl.tudelft.sem.template.activities.domain.Timeslot;
import nl.tudelft.sem.template.activities.domain.Training;
import nl.tudelft.sem.template.activities.domain.TrainingRepository;
import nl.tudelft.sem.template.activities.model.PositionNameRequestModel;
import nl.tudelft.sem.template.activities.model.UpdateRequestDataModel;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ActivityControllerTest {

    private transient ActivityController activityController;

    private transient AuthManager authManager;

    private transient TrainingRepository trainingRepository;

    private transient MatchingClient matchingClient;

    private transient MockMvc mockMvc;

    private final transient Timeslot ts1 = new Timeslot(
            LocalDateTime.of(2042, 12, 23, 10, 0),
            LocalDateTime.of(2042, 12, 23, 17, 15));

    private final transient Timeslot ts2 = new Timeslot(
            LocalDateTime.of(2042, 12, 23, 15, 0),
            LocalDateTime.of(2042, 12, 23, 20, 15));

    private transient Competition c1;

    private transient Competition c2;

    private transient Training t1;

    private transient Training t2;

    @BeforeEach
    void setUp() {
        authManager = Mockito.mock(AuthManager.class);
        trainingRepository = Mockito.mock(TrainingRepository.class);
        matchingClient = Mockito.mock(MatchingClient.class);
        activityController = new ActivityController(authManager, trainingRepository, matchingClient);
        this.mockMvc = MockMvcBuilders.standaloneSetup(activityController).build();
        c1 = new Competition("owner1@gmail.com", 1, 0, 4, 4, 0,
                ts1, "4+", "Male", false, "Proteus");
        c2 = new Competition("owner2@gmail.com", 0, 2, 0, 0, 8,
                ts1, "8+", "Female", true, "Laga");
        t1 = new Training("owner1@gmail.com", 1, 0, 4, 4, 0,
                ts2, "C4");
        t2 = new Training("owner2@gmail.com", 1, 0, 4, 4, 0,
                ts1, "8+");
    }

    @Test
    public void constructorTest() {
        assertThat(activityController).isNotNull();
    }

    @Test
    public void getActivityTimeslotByIdNotFound() throws Exception {
        when(trainingRepository.findById(1L)).thenReturn(Optional.empty());
        this.mockMvc
                .perform(get("/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getActivityTimeslotByIdOk() throws Exception {
        when(trainingRepository.findById(1L)).thenReturn(Optional.of(c1));
        this.mockMvc
                .perform(get("/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllActivitiesWithinTimeslotEmpty() {
        when(trainingRepository.findTrainingsByTimeslot(LocalDateTime.MIN, LocalDateTime.MAX))
                .thenReturn(new ArrayList<Training>());
        List<Training> trainings = activityController.getAllActivitiesWithinTimeslot(
                new Timeslot(LocalDateTime.MIN, LocalDateTime.MAX))
                .getBody().getActivities();
        assertThat(trainings.isEmpty()).isTrue();
    }

    @Test
    void getAllActivitiesWithinTimeslot() {
        List<Training> list = List.of(t1, t2, c2);
        when(trainingRepository.findTrainingsByTimeslot(ts1.getStartTime(), ts1.getEndTime())).thenReturn(list);
        List<Training> trainings = activityController.getAllActivitiesWithinTimeslot(ts1).getBody().getActivities();
        assertThat(trainings.size()).isEqualTo(3);
    }

    @Test
    void getAllActivitiesByOwner() {
        List<Training> list = List.of(t1, c1);
        when(trainingRepository.findTrainingsByOwnerId("owner1@gmail.com")).thenReturn(list);
        when(authManager.getUserId()).thenReturn("owner1@gmail.com");
        List<Training> trainings = activityController.getAllActivitiesByOwner().getBody().getActivities();
        assertThat(trainings.size() == 2).isTrue();
    }

    @Test
    void createCompetition() {
        when(matchingClient.validateCertificate(c1.getCertificate())).thenReturn(true);
        when(authManager.getUserId()).thenReturn(c1.getOwnerId());
        String response = activityController.createCompetition(c1).getBody();
        assertThat(response).isEqualTo("Competition created successfully!");
        verify(trainingRepository).save(c1);
    }

    @Test
    void createTraining() {
        when(matchingClient.validateCertificate(t1.getCertificate())).thenReturn(true);
        when(authManager.getUserId()).thenReturn(t1.getOwnerId());
        String response = activityController.createTraining(t1).getBody();
        assertThat(response).isEqualTo("Training created successfully!");
        verify(trainingRepository).save(t1);
    }

    @Test
    void deleteTraining() {
        when(trainingRepository.findById(1L)).thenReturn(Optional.of(c1));
        when(authManager.getUserId()).thenReturn(c1.getOwnerId());
        String response = activityController.deleteTraining(1L).getBody();
        assertThat(response).isEqualTo("Training with the id 1 has been deleted successfully!");
        verify(trainingRepository).delete(c1);
        verify(matchingClient).deleteAllMatchesForTraining(anyLong());
    }

    @Test
    void updateTraining() {
        UpdateRequestDataModel request = new UpdateRequestDataModel();
        request.setId(1L);
        request.setGender("Female");
        when(trainingRepository.findById(1L)).thenReturn(Optional.of(c1));
        when(authManager.getUserId()).thenReturn(c1.getOwnerId());
        when(matchingClient.validateCertificate(any())).thenReturn(true);
        activityController.updateTraining(request).toString();
        verify(trainingRepository).save(any());
        verify(matchingClient).validateCertificate(c1.getCertificate());
        verify(matchingClient).deleteAllMatchesForTraining(anyLong());
    }

    @Test
    void reduceByOneTest() {
        when(trainingRepository.findById(1L)).thenReturn(Optional.of(t1));
        PositionNameRequestModel name = new PositionNameRequestModel();
        name.setPosition("cox");
        Training training =  activityController.reduceByOne(1L, name).getBody();
        assertThat(t1.getPositions().getCox()).isEqualTo(0);
        verify(trainingRepository).save(t1);
    }

    @Test
    void reduceByOneTestButZero() {
        when(trainingRepository.findById(1L)).thenReturn(Optional.of(t1));
        PositionNameRequestModel name = new PositionNameRequestModel();
        name.setPosition("coach");
        Training training =  activityController.reduceByOne(1L, name).getBody();
        assertThat(t1.getPositions().getCoach()).isEqualTo(0);
        verify(trainingRepository, never()).save(any());
    }
}