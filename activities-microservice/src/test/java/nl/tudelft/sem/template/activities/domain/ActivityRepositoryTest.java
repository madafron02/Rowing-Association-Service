package nl.tudelft.sem.template.activities.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ActivityRepositoryTest {

    @Autowired
    private ActivityRepository activityRepository;

    @BeforeEach
    void setUp() {
        activityRepository.deleteAll();
        Activity a1 = new Activity("owner1@gmail.com", 1, null, null, null, null,
                LocalDateTime.of(2042, 12, 12, 19, 15),
                LocalDateTime.of(2042, 12, 12, 22, 15), "4+", false, null);
        Activity a2 = new Activity("owner2@gmail.com", null, 2, 5, 3, 1,
                LocalDateTime.of(2042, 12, 12, 20, 15),
                LocalDateTime.of(2042, 12, 12, 23, 15), "C4", false, null);
        activityRepository.save(a1);
        activityRepository.save(a2);
    }

    @Test
    void findActivitiesByTimeslotTooNarrow() {
        LocalDateTime startTime = LocalDateTime.of(2042, 12, 12, 20, 15);
        LocalDateTime endTime = LocalDateTime.of(2042, 12, 12, 23, 14);
        assertThat(activityRepository.findActivitiesByTimeslot(startTime, endTime).size()).isEqualTo(0);
    }

    @Test
    void findActivitiesByTimeslotExactBounds() {
        LocalDateTime startTime = LocalDateTime.of(2042, 12, 12, 19, 15);
        LocalDateTime endTime = LocalDateTime.of(2042, 12, 12, 22, 15);
        assertThat(activityRepository.findActivitiesByTimeslot(startTime, endTime).size()).isEqualTo(1);
    }

    @Test
    void findActivitiesByTimeslotMultipleActivities() {
        LocalDateTime startTime = LocalDateTime.of(2042, 12, 12, 19, 15);
        LocalDateTime endTime = LocalDateTime.of(2042, 12, 12, 23, 15);
        assertThat(activityRepository.findActivitiesByTimeslot(startTime, endTime).size()).isEqualTo(2);
    }
}