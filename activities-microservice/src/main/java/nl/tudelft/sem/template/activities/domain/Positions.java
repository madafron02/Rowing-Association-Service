package nl.tudelft.sem.template.activities.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * An embeddable class for storing required positions of an Activity.
 */
@Embeddable
@NoArgsConstructor
@Getter
@Setter
public class Positions {

    @Column(name = "cox")
    private Integer cox;

    @Column(name = "coach")
    private Integer coach;

    @Column(name = "port")
    private Integer port;

    @Column(name = "starboard")
    private Integer starboard;

    @Column(name = "sculling")
    private Integer sculling;

    /**
     * Creates a Positions class.
     *
     * @param cox cox count
     * @param coach coach count
     * @param port port side rowe count
     * @param starboard starboard side rower count
     * @param sculling sculling rower count
     */
    public Positions(Integer cox, Integer coach, Integer port, Integer starboard, Integer sculling) {
        this.cox = cox;
        this.coach = coach;
        this.port = port;
        this.starboard = starboard;
        this.sculling = sculling;
    }

    /**
     * Reduces the value of remaining spots for a certain position by one.
     *
     * @param positionName the name of the position to reduce
     * @return true if it was reduced and false otherwise
     */
    public boolean reduceByOne(String positionName) {
        switch (positionName) {
            case "cox":
                if (this.getCox() > 0) {
                    this.setCox(this.getCox() - 1);
                    return true;
                }
                break;
            case "coach":
                if (this.getCoach() > 0) {
                    this.setCoach(this.getCoach() - 1);
                    return true;
                }
                break;
            case "port":
                if (this.getPort() > 0) {
                    this.setPort(this.getPort() - 1);
                    return true;
                }
                break;
            case "starboard":
                if (this.getStarboard() > 0) {
                    this.setStarboard(this.getStarboard() - 1);
                    return true;
                }
                break;
            case "sculling":
                if (this.getSculling() > 0) {
                    this.setSculling(this.getSculling() - 1);
                    return true;
                }
                break;
            default:
                break;
        }
        return false;
    }

    /**
     * Checks if this object has valid data.
     *
     * @return true if this is valid and false otherwise
     */
    public boolean checkIfValid() {
        return cox != null || coach != null || port != null || starboard != null || sculling != null;
    }
}
