package nl.tudelft.sem.template.matching.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * A DDD repository for quering and persisting matchings.
 */
@Repository
public interface MatchingRepo extends JpaRepository<Match, Long> {

    Optional<Match> getMatchByMatchId(Long matchId);

    List<Match> getMatchesByOwnerId(String ownerId);

}
