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

    @Query(value =
            "SELECT * FROM Match m "
                    + "WHERE m.owner_id == ownerID"
                    + "AND m.status == PENDING",
            nativeQuery = true)
    List<Match> getMatches(@Param("ownerId") String ownerId);

}
