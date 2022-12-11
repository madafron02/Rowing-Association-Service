package nl.tudelft.sem.template.matching.controllers;

import nl.tudelft.sem.template.matching.domain.Match;
import nl.tudelft.sem.template.matching.domain.MatchingService;
import nl.tudelft.sem.template.matching.domain.Status;
import nl.tudelft.sem.template.matching.models.DecisionModel;
import nl.tudelft.sem.template.matching.models.MatchingRequestModel;
import nl.tudelft.sem.template.matching.models.MatchingResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import javax.ws.rs.core.Response;
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
     * API Endpoint for accepting or denying a request of a participant.
     *
     * @param decision DTO containing the matchId and the decision
     * @return message containing whether this action was successful
     */
    @PostMapping("/accept")
    public ResponseEntity<String> acceptOrDenyRequest(@RequestBody DecisionModel decision) {
        if (!service.acceptOrDenyRequest(decision.getMatchId(), decision.isDecision())) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok("Notification sent successfully !");
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
    @GetMapping("/participants")
    public ResponseEntity<List<Match>> getPendingRequests() {
        try {
            return ResponseEntity.ok(service.getPendingRequests());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * API Endpoint used by a user to retrieve all the activities he matched with a specific status.
     *
     * @param status of the required matches
     * @return a list with all the matches of the user that have the required status
     */
    @GetMapping("/match/{status}")
    public ResponseEntity<List<Match>> getMatches(@RequestParam Status status) {
        return ResponseEntity.ok(service.getMatches(status));
    }
}
