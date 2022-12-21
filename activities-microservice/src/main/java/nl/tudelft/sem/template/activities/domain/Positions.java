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
}
