package nl.tudelft.sem.template.activities.domain;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * A DDD JPA repository for the Competition entity.
 */
@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Long> {

    @Query(value = "SELECT * FROM competitions WHERE start_time >= ?1 AND end_time <= ?2",
            nativeQuery = true)
    List<Competition> findCompetitionsByTimeslot(
            LocalDateTime startTime, LocalDateTime endTime
    );

    List<Competition> findCompetitionsByOwnerId(String ownerId);
}
