package nl.tudelft.sem.template.matching.domain.database;

import nl.tudelft.sem.template.matching.domain.Match;
import nl.tudelft.sem.template.matching.domain.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * A DDD repository for querying and persisting matching.
 */
@Repository
public interface MatchingRepo extends JpaRepository<Match, Long> {

    Optional<Match> getMatchByMatchId(Long matchId);

    //List<Match> getMatchesByOwnerId(String ownerId);

    List<Match> getMatchesByParticipantIdAndStatus(String participantId, Status status);

    //List<Match> getMatchesByOwnerIdAndStatus(String ownerId, Status status);

    List<Match> getMatchesByActivityInformation_OwnerIdAndStatus(String ownerId, Status status);

    //void deleteMatchesByActivityId(Long activityId);

    //List<Match> getMatchesByActivityId(Long activityId);

    List<Match> getMatchesByActivityInformation_ActivityId(Long activityId);

    //Optional<Match> getMatchesByActivityIdAndParticipantId(Long activityId, String participantId);

    Optional<Match> getMatchByActivityInformation_ActivityIdAndParticipantId(Long activityId, String participantId);
}
