package nl.tudelft.sem.template.activities.controllers;

import nl.tudelft.sem.template.activities.authentication.AuthManager;
import nl.tudelft.sem.template.activities.domain.Activity;
import nl.tudelft.sem.template.activities.domain.ActivityRepository;
import nl.tudelft.sem.template.activities.domain.MatchingClient;
import nl.tudelft.sem.template.activities.domain.Timeslot;
import nl.tudelft.sem.template.activities.model.ActivityListResponseModel;
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

    private final transient ActivityRepository activityRepository;

    private final transient MatchingClient matchingClient;

    /**
     * Instantiates a new controller.
     *
     * @param authManager Spring Security component used to authenticate and authorize the user
     */
    @Autowired
    public ActivityController(
            AuthManager authManager,
            ActivityRepository activityRepository,
            MatchingClient matchingClient
    ) {
        this.authManager = authManager;
        this.activityRepository = activityRepository;
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
        Optional<Activity> activity = activityRepository.findById(id);
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
        List<Activity> activities = activityRepository.findActivitiesByTimeslot(
                request.getStartTime(), request.getEndTime()
        );
        return ResponseEntity.ok(new ActivityListResponseModel(activities));
    }

    /**
     * Gets all Activities created by the client User.
     *
     * @return a list of Activities created by the client.
     */
    @GetMapping("/list")
    public ResponseEntity<ActivityListResponseModel> getAllActivitiesByOwner() {
        List<Activity> activities = activityRepository.findActivitiesByOwnerId(authManager.getUserId());
        return ResponseEntity.ok(new ActivityListResponseModel(activities));
    }

    /**
     * Gets example by id.
     *
     * @return the example found in the database with the given id
     */
    @PostMapping("/publish")
    public ResponseEntity<String> createActivity(@RequestBody Activity request) {
        if (!request.checkIfValid()) {
            return ResponseEntity.badRequest().build();
        }
        request.setOwnerId(authManager.getUserId());
        activityRepository.save(request);
        return ResponseEntity.ok("Activity created successfully!");
    }

    /**
     * Deletes an Activity by its given id.
     *
     * @param activityId the id of the Activity
     * @return a response entity showing if the Activity was deleted
     */
    @DeleteMapping("/delete/{activityId}")
    public ResponseEntity<String> deleteActivity(@PathVariable Long activityId) {
        if (activityId == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Activity> toDelete = activityRepository.findById(activityId);
        if (toDelete.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        if (!toDelete.get().getOwnerId().equals(authManager.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        activityRepository.delete(toDelete.get());
        matchingClient.deleteAllMatches(activityId);
        return ResponseEntity.ok("Activity with the id " + activityId + " has been deleted successfully!");
    }

    /**
     * Updates the attributes of an Activity.
     *
     * @param request the values which will be used for updating the Activity
     * @return the Activity that was updated
     */
    @PatchMapping("/edit")
    public ResponseEntity<Activity> updateActivity(@RequestBody Activity request) {
        Optional<Activity> toUpdate = activityRepository.findById(request.getId());
        if (toUpdate.isEmpty()) {
            return new ResponseEntity("Activity with the id: " + request.getId() + " was not found.", HttpStatus.NOT_FOUND);
        }
        Activity activity = toUpdate.get();
        activity.updateFields(request);
        if (!activity.getOwnerId().equals(authManager.getUserId())) {
            return new ResponseEntity("The access to this activity is forbidden", HttpStatus.FORBIDDEN);
        }
        if (!activity.checkIfValid()) {
            return new ResponseEntity("Update failed: the attributes have incorrect values.", HttpStatus.BAD_REQUEST);
        }
        activityRepository.save(activity);
        matchingClient.deleteAllMatches(activity.getId());
        return ResponseEntity.ok(activity);
    }

    /**
     * Reduce the number of remaining spots for a given position of an activity.
     *
     * @param activityId the id of an activity
     * @param position the position which should have the reduced count
     * @return a response entity containing the activity
     */
    @PutMapping("/update/{activityId}")
    public ResponseEntity<Activity> reduceByOne(@PathVariable Long activityId, @RequestBody String position) {
        Optional<Activity> toDelete = activityRepository.findById(activityId);
        if (toDelete.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Activity activity = toDelete.get();
        if (!activity.getPositions().reduceByOne(position)) {
            return ResponseEntity.badRequest().build();
        }
        activityRepository.save(activity);
        return ResponseEntity.ok(activity);
    }
}
