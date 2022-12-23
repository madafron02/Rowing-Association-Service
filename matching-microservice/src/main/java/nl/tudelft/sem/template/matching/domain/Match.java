package nl.tudelft.sem.template.matching.domain;


import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "matches")
@NoArgsConstructor
public class Match {

    /**
     * Entity that depicts the match being made by the system
     * between an user with an availability and a certain activity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private long matchId;

    @Column(name = "participant_id", nullable = false)
    private String participantId;

    @Column(name = "acivity_id", nullable = false)
    private long activityId;

    @Column(name = "owner_id", nullable = false)
    private String ownerId;

    @Column(name = "position", nullable = false)
    private String position;

    @Column(name = "status", nullable = false)
    private Status status;

    /**
     * Constructor of the Match Entity.
     *
     * @param participantId the id of the user -> participant
     * @param activityId    the id of the activity the user was matched with
     * @param ownerId       the id of the user -> owner
     * @param position      the position the user wants
     */
    public Match(String participantId, long activityId, String ownerId, String position) {
        this.participantId = participantId;
        this.activityId = activityId;
        this.ownerId = ownerId;
        this.position = position;
        this.status = Status.MATCHED;
    }

    /**
     * Method used when the match status is updated during the matching process.
     *
     * @param status new status of match
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Getter for the id of a match.
     *
     * @return id of a match
     */
    public long getMatchId() {
        return matchId;
    }

    /**
     * Getter for the participant id.
     *
     * @return the email of the participant
     */
    public String getParticipantId() {
        return participantId;
    }

    /**
     * Getter for the activity id.
     *
     * @return the id of the activity matched
     */
    public long getActivityId() {
        return activityId;
    }

    /**
     * Getter for the owner id.
     *
     * @return email of the owner of activity
     */
    public String getOwnerId() {
        return ownerId;
    }

    /**
     * Getter for the position occupied by the participant.
     *
     * @return a String representing the position
     */
    public String getPosition() {
        return position;
    }

    /**
     * Getter for the status of the match.
     *
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Equals method for comparing two objects.
     *
     * @param o the object to compare it to
     * @return TRUE if the Object is a Match Entity with same id
     *         FALSE otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Match)) {
            return false;
        }
        Match match = (Match) o;
        return matchId == match.matchId;
    }


    /**
     * Method for hashing the Match Entity.
     *
     * @return hashed code
     */
    @Override
    public int hashCode() {
        return Objects.hash(matchId);
    }
}
