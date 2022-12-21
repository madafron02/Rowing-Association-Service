package nl.tudelft.sem.template.activities.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * An embeddable class for storing required positions of an Activity.
 */
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Positions {

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

    /**
     * Reduces the value of remaining spots for a certain position by one.
     *
     * @param positionName the name of the position to reduce
     * @return true if it was reduced and false otherwise
     */
    public boolean reduceByOne(String positionName) {
        String[] parts = positionName.split("\"");
        String name = parts[3];
        switch (name) {
            case "cox":
                if (this.getCoxCount() > 0) {
                    this.setCoxCount(this.getCoxCount() - 1);
                    return true;
                }
                break;
            case "coach":
                if (this.getCoachCount() > 0) {
                    this.setCoachCount(this.getCoachCount() - 1);
                    return true;
                }
                break;
            case "port":
                if (this.getPortSideRowerCount() > 0) {
                    this.setPortSideRowerCount(this.getPortSideRowerCount() - 1);
                    return true;
                }
                break;
            case "starboard":
                if (this.getPortSideRowerCount() > 0) {
                    this.setStarboardSideRowerCount(this.getStarboardSideRowerCount() - 1);
                    return true;
                }
                break;
            case "sculling":
                if (this.getScullingRowerCount() > 0) {
                    this.setScullingRowerCount(this.getScullingRowerCount() - 1);
                    return true;
                }
                break;
            default:
                break;
        }
        return false;
    }
}
