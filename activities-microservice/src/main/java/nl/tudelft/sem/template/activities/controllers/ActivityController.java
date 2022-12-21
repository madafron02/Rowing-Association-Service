package nl.tudelft.sem.template.activities.controllers;

import java.util.List;
import java.util.Optional;

import nl.tudelft.sem.template.activities.authentication.AuthManager;
import nl.tudelft.sem.template.activities.domain.Training;
import nl.tudelft.sem.template.activities.domain.Competition;
import nl.tudelft.sem.template.activities.domain.TrainingRepository;
import nl.tudelft.sem.template.activities.domain.MatchingClient;
import nl.tudelft.sem.template.activities.domain.Timeslot;
import nl.tudelft.sem.template.activities.model.ActivityListResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Activity querying controller.
 * <p>
 * This controller shows how you can extract information from the JWT token.
 * </p>
 */
@RestController
@RequestMapping("/activities")
public class ActivityController {

    private final transient AuthManager authManager;

    private final transient TrainingRepository trainingRepository;

    private final transient MatchingClient matchingClient;

    /**
     * Instantiates a new controller.
     *
     * @param authManager Spring Security component used to authenticate and authorize the user
     */
    @Autowired
    public ActivityController(
            AuthManager authManager,
            TrainingRepository trainingRepository,
            MatchingClient matchingClient
    ) {
        this.authManager = authManager;
        this.trainingRepository = trainingRepository;
        this.matchingClient = matchingClient;
    }

    /**
     * Gets all activities within a given timeslot.
     *
     * @return response entity containing every activity in the timeslot
     */
    @GetMapping("/within-timeslot")
    public ResponseEntity<ActivityListResponseModel> getAllActivitiesWithinTimeslot(@RequestBody Timeslot request) {
        List<Training> trainings = trainingRepository.findTrainingsByTimeslot(
                request.getStartTime(), request.getEndTime()
        );
        return ResponseEntity.ok(new ActivityListResponseModel(trainings));
    }

    /**
     * Gets all Activities created by the client User.
     *
     * @return a list of Activities created by the client.
     */
    @GetMapping("/list")
    public ResponseEntity<ActivityListResponseModel> getAllActivitiesByOwner() {
        List<Training> trainings = trainingRepository.findTrainingsByOwnerId(authManager.getUserId());
        return ResponseEntity.ok(new ActivityListResponseModel(trainings));
    }

    /**
     * Creates a new Competition.
     *
     * @return a response entity showing if the Competition was created
     */
    @PostMapping("/publish-competition")
    public ResponseEntity<String> createCompetition(@RequestBody Competition request) {
        request.setOwnerId(authManager.getUserId());
        if (!request.checkIfValid()) {
            return ResponseEntity.badRequest().build();
        }
        trainingRepository.save(request);
        return ResponseEntity.ok("Competition created successfully!");
    }

    /**
     * Creates a new Training.
     *
     * @return a response entity showing if the Training was created
     */
    @PostMapping("/publish-training")
    public ResponseEntity<String> createTraining(@RequestBody Training request) {
        request.setOwnerId(authManager.getUserId());
        if (!request.checkIfValid()) {
            return ResponseEntity.badRequest().build();
        }
        trainingRepository.save(request);
        return ResponseEntity.ok("Training created successfully!");
    }

    /**
     * Deletes a Training by its given id.
     *
     * @param trainingId the id of the Training
     * @return a response entity showing if the Training was deleted
     */
    @DeleteMapping("/delete-training/{trainingId}")
    public ResponseEntity<String> deleteTraining(@PathVariable Long trainingId) {
        if (trainingId == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Training> toDelete = trainingRepository.findById(trainingId);
        if (toDelete.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        if (!toDelete.get().getOwnerId().equals(authManager.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        trainingRepository.delete(toDelete.get());
        matchingClient.deleteAllMatchesForTraining(trainingId);
        return ResponseEntity.ok("Training with the id " + trainingId + " has been deleted successfully!");
    }
}
