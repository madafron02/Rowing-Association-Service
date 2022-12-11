package nl.tudelft.sem.template.activities.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.activities.model.ActivityDataModel;

/**
 * A DDD entity representing an activity in our domain.
 */
@Entity
@Table(name = "users")
@NoArgsConstructor
@EqualsAndHashCode
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "ownerId", nullable = false)
    private String ownerId;

    @Column(name = "coxCount")
    private Integer coxCount;

    @Column(name = "coachCount")
    private Integer coachCount;

    @Column(name = "portSideRowerCount")
    private Integer portSideRowerCount;

    @Column(name = "starboardSideRowerCount")
    private Integer starboardSideRowerCount;

    @Column(name = "scullingRowerCount")
    private Integer scullingRowerCount;

    @Column(name = "startTime", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "endTime", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "certificate")
    private String certificate;

    @Column(name = "competition", nullable = false)
    private Boolean competition;

    @Column(name = "gender")
    private String gender;

    /**
     * Creates a new Activity.
     *
     * @param ownerId the email of the user that created the activity
     * @param coxCount number of needed coxes
     * @param coachCount number of needed coaches
     * @param portSideRowerCount number of needed port side rowers
     * @param starboardSideRowerCount number of needed starboard side
     * @param scullingRowerCount number of needed sculling rowers
     * @param startTime starting time of the activity
     * @param endTime ending time of the activity
     * @param certificate the boat type
     * @param competition true if it is a competition and false otherwise
     * @param gender the gender of the needed participants, null if not needed
     */
    public Activity(String ownerId, Integer coxCount, Integer coachCount, Integer portSideRowerCount,
                    Integer starboardSideRowerCount, Integer scullingRowerCount, LocalDateTime startTime,
                    LocalDateTime endTime, String certificate, Boolean competition, String gender) {
        this.ownerId = ownerId;
        this.coxCount = coxCount;
        this.coachCount = coachCount;
        this.portSideRowerCount = portSideRowerCount;
        this.starboardSideRowerCount = starboardSideRowerCount;
        this.scullingRowerCount = scullingRowerCount;
        this.startTime = startTime;
        this.endTime = endTime;
        this.certificate = certificate;
        this.competition = Objects.requireNonNullElse(competition, false);
        this.gender = gender;
    }

    /**
     * Constructs a new activity from a request data model.
     *
     * @param dataModel the activity data from the request
     */
    public Activity(ActivityDataModel dataModel) {
        this.ownerId = dataModel.getOwnerId();
        this.coxCount = dataModel.getCoxCount();
        this.coachCount = dataModel.getCoachCount();
        this.portSideRowerCount = dataModel.getPortSideRowerCount();
        this.starboardSideRowerCount = dataModel.getStarboardSideRowerCount();
        this.scullingRowerCount = dataModel.getScullingRowerCount();
        this.startTime = dataModel.getStartTime();
        this.endTime = dataModel.getEndTime();
        this.certificate = dataModel.getCertificate();
        this.competition = dataModel.isCompetition();
        this.gender = dataModel.getGender();
    }

    /**
     * Checks if this activity has valid data.
     *
     * @return true if this is valid and false otherwise
     */
    public boolean checkIfValid() {
        boolean requiresRowers = coxCount != null || coachCount != null || portSideRowerCount != null
                || starboardSideRowerCount != null || scullingRowerCount != null;
        boolean nonNull = ownerId != null && requiresRowers && startTime != null;
        if (!nonNull) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        return startTime.isBefore(endTime) && startTime.isAfter(now);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getCoxCount() {
        return coxCount;
    }

    public void setCoxCount(Integer coxCount) {
        this.coxCount = coxCount;
    }

    public Integer getCoachCount() {
        return coachCount;
    }

    public void setCoachCount(Integer coachCount) {
        this.coachCount = coachCount;
    }

    public Integer getPortSideRowerCount() {
        return portSideRowerCount;
    }

    public void setPortSideRowerCount(Integer portSideRowerCount) {
        this.portSideRowerCount = portSideRowerCount;
    }

    public Integer getStarboardSideRowerCount() {
        return starboardSideRowerCount;
    }

    public void setStarboardSideRowerCount(Integer starboardSideRowerCount) {
        this.starboardSideRowerCount = starboardSideRowerCount;
    }

    public Integer getScullingRowerCount() {
        return scullingRowerCount;
    }

    public void setScullingRowerCount(Integer scullingRowerCount) {
        this.scullingRowerCount = scullingRowerCount;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public boolean isCompetition() {
        return competition;
    }

    public void setCompetition(boolean competition) {
        this.competition = competition;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
