package nl.tudelft.sem.template.matching.domain;

import nl.tudelft.sem.template.matching.authentication.AuthManager;
import nl.tudelft.sem.template.matching.domain.database.MatchingRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Sanitization {

    private final transient AuthManager auth;
    private final transient MatchingRepo matchingRepo;

    public Sanitization(AuthManager auth, MatchingRepo matchingRepo) {
        this.auth = auth;
        this.matchingRepo = matchingRepo;
    }

    /**
     * Method for verifying the identity of the user making a request by using the SecurityContext
     * in the auth manager.
     *
     * @param matchId the id of the match
     * @return true if the user making the request has the same userIs as the userId of the match given
     *         false otherwise
     */
    private boolean verifyUser(long matchId) {
        return auth.getUserId().equals(matchingRepo.getMatchByMatchId(matchId).get().getParticipantId());
    }

    /**
     * Method for verifying the existence of the match.
     *
     * @param matchId the id of the match
     * @return true if it exists in the database a match with the id given
     *         false otherwise
     */
    public boolean verifyMatch(long matchId) {
        return matchingRepo.getMatchByMatchId(matchId).isPresent() && verifyUser(matchId);
    }

    /**
     * Method for verifying whether a position is valid.
     *
     * @param position to check
     * @return true iff the position exists
     */
    public boolean verifyPosition(String position) {
        List<String> validPositions = List.of("cox", "port", "coach", "starboard", "sculling");
        return validPositions.contains(position);
    }

    /**
     * Method for retrieving all matches of the user with the specified status.
     *
     * @param status of the required activities
     * @return list of required matches
     */
    public List<Match> getMatches(Status status) {
        String userId = auth.getUserId();
        return matchingRepo.getMatchesByParticipantIdAndStatus(userId, status);
    }

    /**
     * Method for getting the pending requests by the current userId acting as owner of activities.
     *
     * @return the List of matches being in pending for the owner (client making a request)
     */
    public List<Match> getPendingRequests() {
        return matchingRepo.getMatchesByOwnerIdAndStatus(auth.getUserId(), Status.PENDING);
    }
}
