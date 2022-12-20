package nl.tudelft.sem.template.activities.controllers;

import nl.tudelft.sem.template.activities.authentication.AuthManager;
import nl.tudelft.sem.template.activities.domain.Activity;
import nl.tudelft.sem.template.activities.domain.ActivityRepository;
import nl.tudelft.sem.template.activities.domain.Timeslot;
import nl.tudelft.sem.template.activities.model.ActivityListResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    /**
     * Instantiates a new controller.
     *
     * @param authManager Spring Security component used to authenticate and authorize the user
     */
    @Autowired
    public ActivityController(AuthManager authManager, ActivityRepository activityRepository) {
        this.authManager = authManager;
        this.activityRepository = activityRepository;
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
     * Gets example by id.
     *
     * @return the example found in the database with the given id
     */
    @PostMapping("/publish")
    public ResponseEntity<String> createActivity(@RequestBody Activity request) {
        if (!request.checkIfValid()) {
            return ResponseEntity.badRequest().build();
        }
        activityRepository.save(request);
        return ResponseEntity.ok("Activity created successfully!");
    }
}
