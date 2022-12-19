package nl.tudelft.sem.template.activities.domain;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * A DDD JPA repository for the Activity entity.
 */
@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    @Query(value = "SELECT * FROM activities WHERE start_time >= ?1 AND end_time <= ?2",
            nativeQuery = true)
    List<Activity> findActivitiesByTimeslot(
            LocalDateTime startTime, LocalDateTime endTime
    );
}
