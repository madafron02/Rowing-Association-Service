package nl.tudelft.sem.template.activities.controllers;

import java.util.List;
import nl.tudelft.sem.template.activities.authentication.AuthManager;
import nl.tudelft.sem.template.activities.domain.Activity;
import nl.tudelft.sem.template.activities.domain.ActivityRepository;
import nl.tudelft.sem.template.activities.model.ActivityDataModel;
import nl.tudelft.sem.template.activities.model.ActivityListResponseModel;
import nl.tudelft.sem.template.activities.model.TimeslotDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    @GetMapping("/within-timeslot")
    public ResponseEntity<ActivityListResponseModel> getAllActivitiesWithinTimeslot(@RequestBody TimeslotDataModel request) {
        List<Activity> activities = activityRepository.findActivitiesByStartTimeGreaterThanEqualAndEndTimeLessThanEqual(
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
    public ResponseEntity<String> createActivity(@RequestBody ActivityDataModel request) {
        Activity activity = new Activity(request);
        if (!activity.checkIfValid()) {
            return ResponseEntity.badRequest().build();
        }
        activityRepository.save(activity);
        return ResponseEntity.ok("Activity created successfully!");
    }
}
