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
        Timeslot t1 = new Timeslot(LocalDateTime.of(2042, 12, 12, 19, 15),
                LocalDateTime.of(2042, 12, 12, 22, 15));
        Timeslot t2 = new Timeslot(LocalDateTime.of(2042, 12, 12, 20, 15),
                LocalDateTime.of(2042, 12, 12, 23, 15));
        Activity a1 = new Activity("owner1@gmail.com", new Positions(1, null, null, null, null),
                t1, "4+", false, null, "Laga");
        Activity a2 = new Activity("owner2@gmail.com", new Positions(null, 2, 5, 3, 1),
                t2, "C4", false, null, "Laga");
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

    @Test
    void findActivityByOwnerIdTest() {
        assertThat(activityRepository.findActivitiesByOwnerId("owner1@gmail.com").size()).isEqualTo(1);
    }

    @Test
    void findActivityByOwnerIdNotFoundTest() {
        assertThat(activityRepository.findActivitiesByOwnerId("non-existent@gmail.com").size()).isEqualTo(0);
    }
}