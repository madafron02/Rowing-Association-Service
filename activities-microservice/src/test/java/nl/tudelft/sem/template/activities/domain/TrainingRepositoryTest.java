package nl.tudelft.sem.template.activities.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TrainingRepositoryTest {

    @Autowired
    private transient TrainingRepository trainingRepository;

    @BeforeEach
    void setUp() {
        trainingRepository.deleteAll();
        Training a1 = new Training("owner1@gmail.com", 1, 0, 0, 0, 0,
                LocalDateTime.of(2042, 12, 12, 19, 15),
                LocalDateTime.of(2042, 12, 12, 22, 15), "4+");
        Training a2 = new Training("owner2@gmail.com", 0, 2, 5, 3, 1,
                LocalDateTime.of(2042, 12, 12, 20, 15),
                LocalDateTime.of(2042, 12, 12, 23, 15), "C4");
        trainingRepository.save(a1);
        trainingRepository.save(a2);
    }

    @Test
    void findActivitiesByTimeslotTooNarrow() {
        LocalDateTime startTime = LocalDateTime.of(2042, 12, 12, 20, 15);
        LocalDateTime endTime = LocalDateTime.of(2042, 12, 12, 23, 14);
        assertThat(trainingRepository.findTrainingsByTimeslot(startTime, endTime).size()).isEqualTo(0);
    }

    @Test
    void findActivitiesByTimeslotExactBounds() {
        LocalDateTime startTime = LocalDateTime.of(2042, 12, 12, 19, 15);
        LocalDateTime endTime = LocalDateTime.of(2042, 12, 12, 22, 15);
        assertThat(trainingRepository.findTrainingsByTimeslot(startTime, endTime).size()).isEqualTo(1);
    }

    @Test
    void findActivitiesByTimeslotMultipleActivities() {
        LocalDateTime startTime = LocalDateTime.of(2042, 12, 12, 19, 15);
        LocalDateTime endTime = LocalDateTime.of(2042, 12, 12, 23, 15);
        assertThat(trainingRepository.findTrainingsByTimeslot(startTime, endTime).size()).isEqualTo(2);
    }

    @Test
    void findActivityByOwnerIdTest() {
        assertThat(trainingRepository.findTrainingsByOwnerId("owner1@gmail.com").size()).isEqualTo(1);
    }

    @Test
    void findActivityByOwnerIdNotFoundTest() {
        assertThat(trainingRepository.findTrainingsByOwnerId("non-existent@gmail.com").size()).isEqualTo(0);
    }
}