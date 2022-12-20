package nl.tudelft.sem.template.activities.domain;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * A DDD JPA repository for the Training entity.
 */
@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {

    @Query(value = "SELECT * FROM trainings WHERE start_time >= ?1 AND end_time <= ?2",
            nativeQuery = true)
    List<Training> findTrainingsByTimeslot(
            LocalDateTime startTime, LocalDateTime endTime
    );

    List<Training> findTrainingsByOwnerId(String ownerId);
}
