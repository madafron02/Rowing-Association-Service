package nl.tudelft.sem.template.activities.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    List<Activity> findActivitiesByStartTimeGreaterThanEqualAndEndTimeLessThanEqual(
            LocalDateTime startTime, LocalDateTime endTime
    );
}
