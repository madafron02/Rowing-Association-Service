package nl.tudelft.sem.template.matching.domain;

import lombok.Value;

/**
 * A DDD value object representing an activity.
 */
@Value
public class ActivityApp {
    private long activityId;
    private String publisherId;
    public TimeslotApp timeslot;
    private String gender;
    private String organisation;
    private boolean competitiveness;
}
