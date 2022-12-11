package nl.tudelft.sem.template.matching.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * A DDD repository for querying and persisting matching.
 */
@Repository
public interface MatchingRepo extends JpaRepository<Match, Long> {

    Optional<Match> getMatchByMatchId(Long matchId);

    List<Match> getMatchesByOwnerId(String ownerId);

    List<Match> getMatchesByParticipantIdAndStatus(String ownerId, Status status);

    List<Match> getMatchesByOwnerIdAndStatus(String ownerId, Status status);

}
