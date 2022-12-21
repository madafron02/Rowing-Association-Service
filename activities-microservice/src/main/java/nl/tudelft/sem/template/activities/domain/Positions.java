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
}
