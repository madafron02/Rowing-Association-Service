package nl.tudelft.sem.template.activities.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * An embeddable class for storing required positions of a training or a competition.
 */
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Positions {

    @Column(name = "cox", nullable = false)
    private Integer cox;

    @Column(name = "coach", nullable = false)
    private Integer coach;

    @Column(name = "port", nullable = false)
    private Integer port;

    @Column(name = "starboard", nullable = false)
    private Integer starboard;

    @Column(name = "sculling", nullable = false)
    private Integer sculling;

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
}
