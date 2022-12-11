package nl.tudelft.sem.template.activities.model;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * Model representing an activity's data.
 */
@Data
public class ActivityDataModel {
    private String ownerId;
    private Integer coxCount;
    private Integer coachCount;
    private Integer portSideRowerCount;
    private Integer starboardSideRowerCount;
    private Integer scullingRowerCount;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String certificate;
    private Boolean competition;
    private String gender;
}
