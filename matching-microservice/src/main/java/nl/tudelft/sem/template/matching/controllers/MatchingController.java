package nl.tudelft.sem.template.matching.controllers;

import nl.tudelft.sem.template.matching.domain.Match;
import nl.tudelft.sem.template.matching.domain.MatchingService;
import nl.tudelft.sem.template.matching.models.MatchingRequestModel;
import nl.tudelft.sem.template.matching.models.MatchingResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.Arrays;
import java.util.List;

/**
 * REST API controller for receiving requests regarding the matching functionality.
 */
@RestController
@RequestMapping("/matching")
public class MatchingController {

    private final transient MatchingService service;

    /**
     * Instantiates a new controller.
     *
     * @param service DDD microservice for matching
     */
    @Autowired
    public MatchingController(MatchingService service) {
        this.service = service;
    }

    /**
     * API Endpoint for getting available activities from the activity microservice based on
     * availability and position, provided by the client in the request.
     *
     * @param request DTO containing availability and position
     * @return list of suitable activities
     */
    @PostMapping("/submit")
    public ResponseEntity<MatchingResponseModel> submitAvailability(@RequestBody MatchingRequestModel request) {
        try {
            return ResponseEntity.ok(service.submitAvailability(request.getTimeslot(), request.getPosition()));
        } catch (Exception e) {
            System.err.println(Arrays.toString(e.getStackTrace()));
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * API Endpoint used by a user for picking an activity to participate to.
     *
     * @param matchId the match id
     * @return a String representing saying "Application sent" if the activity
     *         was successfully picked
     */
    @PostMapping("/pick")
    public ResponseEntity<String> pickActivity(@RequestBody long matchId) {
        if (service.verifyMatch(matchId)) {
            service.pickActivity(matchId);
            return ResponseEntity.ok("Application sent");
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * API Endpoint used by the owner of the activity to retrieve all the
     * activities with status pending.
     *
     * @return a list with all the matches that are in pending
     */
    @GetMapping("/pending")
    public ResponseEntity<List<Match>> getPendingRequests() {
        try {
            return ResponseEntity.ok(service.getPendingRequests());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
