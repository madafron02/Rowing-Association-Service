package nl.tudelft.sem.template.matching.controllers;

import nl.tudelft.sem.template.matching.domain.Certificate;
import nl.tudelft.sem.template.matching.domain.Match;
import nl.tudelft.sem.template.matching.domain.MatchingService;
import nl.tudelft.sem.template.matching.domain.Status;
import nl.tudelft.sem.template.matching.domain.database.CertificateRepo;
import nl.tudelft.sem.template.matching.models.DecisionModel;
import nl.tudelft.sem.template.matching.models.MatchingRequestModel;
import nl.tudelft.sem.template.matching.models.MatchingResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST API controller for receiving requests regarding the matching functionality.
 */
@RestController
@RequestMapping("/matching")
public class MatchingController {

    private final transient MatchingService service;
    private final transient CertificateRepo certificateRepo;

    /**
     * Instantiates a new controller.
     *
     * @param service DDD microservice for matching
     */
    @Autowired
    public MatchingController(MatchingService service,
                              CertificateRepo certificateRepo) {
        this.service = service;
        this.certificateRepo = certificateRepo;
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
        if (!service.verifyPosition(request.getPosition())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(service.submitAvailability(request.getTimeslot(), request.getPosition()));

    }

    /**
     * API Endpoint for accepting or denying a request of a participant.
     *
     * @param decision DTO containing the matchId and the decision
     * @return message containing whether this action was successful
     */
    @PostMapping("/decide")
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
    public ResponseEntity<String> pickActivity(@RequestBody Long matchId) {
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
        return ResponseEntity.ok(service.getPendingRequests());
    }

    /**
     * API Endpoint used by a user to retrieve all the activities he matched with a specific status.
     *
     * @param status of the required matches
     * @return a list with all the matches of the user that have the required status
     */
    @GetMapping("/match/{status}")
    public ResponseEntity<List<Match>> getMatches(@PathVariable("status") String status) {
        try {
            Status statusEnum = Status.valueOf(status);
            return ResponseEntity.ok(service.getMatches(statusEnum));
        } catch(Exception e) {
            return new ResponseEntity("Use a valid status (MATCHED, PENDING, ACCEPTED, DECLINED", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * API Endpoint used by the Notification microservice to discard all the matches done
     * for a modified activity.
     *
     * @param activityId the id of the activity modifies
     * @return an okay response entity
     */
    @PostMapping("/activity/modified")
    public ResponseEntity<String> discardMatchesByActivity(@RequestBody Long activityId) {
        service.discardMatchesByActivity(activityId);
        return ResponseEntity.ok("Participants notified successfully !");
    }

    /**
     * API Endpoint used by other microservices to sanitize the certificate input.
     *
     * @param certificate name to validate
     * @return true iff the certificate name is valid
     */
    @PostMapping("/certificate/validate")
    public ResponseEntity<Boolean> validateCertificate(@RequestBody String certificate) {
        return ResponseEntity.ok(certificateRepo.getCertificateByName(certificate).isPresent());
    }

    /**
     * API Endpoint for adding a new certificate to the certificate repository.
     *
     * @param certificate the name of the certificate to be added
     * @return a response entity with a String containing a success/failure message for the client
     */
    @PostMapping("/certificate/add")
    public ResponseEntity addNewCertificate(@RequestBody String certificate) {
        if (certificateRepo.getCertificateByName(certificate).isPresent()) {
            return new ResponseEntity("Certificate already added!", HttpStatus.BAD_REQUEST);
        } else {
            certificateRepo.save(new Certificate(certificate));
            return new ResponseEntity("Certificate successfully added!", HttpStatus.OK);
        }
    }
}
