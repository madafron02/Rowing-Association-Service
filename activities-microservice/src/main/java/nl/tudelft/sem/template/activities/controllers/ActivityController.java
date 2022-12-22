package nl.tudelft.sem.template.activities.controllers;

import nl.tudelft.sem.template.activities.authentication.AuthManager;
import nl.tudelft.sem.template.activities.domain.Training;
import nl.tudelft.sem.template.activities.domain.Competition;
import nl.tudelft.sem.template.activities.domain.TrainingRepository;
import nl.tudelft.sem.template.activities.domain.MatchingClient;
import nl.tudelft.sem.template.activities.domain.Timeslot;
import nl.tudelft.sem.template.activities.model.ActivityListResponseModel;
import nl.tudelft.sem.template.activities.model.PositionNameRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

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
     * Returns the timeslot of an Activity given its id.
     *
     * @param id the id of an Activity
     * @return the Timeslot of the Activity
     */
    @GetMapping("/{id}")
    public ResponseEntity<Timeslot> getActivityTimeslotById(@PathVariable("id") long id) {
        Optional<Training> activity = trainingRepository.findById(id);
        if (activity.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(activity.get().getTimeslot());
    }

    /**
     * Gets all activities within a given timeslot.
     *
     * @return response entity containing every activity in the timeslot
     */
    @PostMapping("/within-timeslot")
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

    /**
     * Updates the attributes of an Activity.
     *
     * @param request the values which will be used for updating the Training
     * @return the Training that was updated
     */
    @PatchMapping("/edit")
    public ResponseEntity<Training> updateTraining(@RequestBody Training request) {
        Optional<Training> toUpdate = trainingRepository.findById(request.getId());
        if (toUpdate.isEmpty()) {
            return new ResponseEntity("Activity with the id: " + request.getId() + " was not found.",
                    HttpStatus.NOT_FOUND);
        }
        Training training = toUpdate.get();
        training.updateFields(request);
        if (!training.checkIfValid()) {
            return new ResponseEntity("Update failed: the at least one of the attributes has incorrect values.",
                    HttpStatus.BAD_REQUEST);
        }
        trainingRepository.save(training);
        matchingClient.deleteAllMatchesForTraining(training.getId());
        return ResponseEntity.ok(training);
    }

    /**
     * Reduce the number of remaining spots for a given position of an activity.
     *
     * @param trainingId the id of an activity
     * @param position the position which should have the reduced count
     * @return a response entity containing the activity
     */
    @PutMapping("/update/{trainingId}")
    public ResponseEntity<Training> reduceByOne(@PathVariable Long trainingId,
                                                @RequestBody PositionNameRequestModel position) {
        Optional<Training> toUpdate = trainingRepository.findById(trainingId);
        if (toUpdate.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Training training = toUpdate.get();
        if (!training.getPositions().reduceByOne(position.getPosition())) {
            return ResponseEntity.badRequest().build();
        }
        trainingRepository.save(training);
        return ResponseEntity.ok(training);
    }
}
