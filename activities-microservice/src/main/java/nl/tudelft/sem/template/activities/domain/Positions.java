package nl.tudelft.sem.template.activities.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.NonNull;

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

    @Column(name = "coxCount")
    @NonNull
    private Integer coxCount;

    @Column(name = "coachCount")
    @NonNull
    private Integer coachCount;

    @Column(name = "portSideRowerCount")
    @NonNull
    private Integer portSideRowerCount;

    @Column(name = "starboardSideRowerCount")
    @NonNull
    private Integer starboardSideRowerCount;

    @Column(name = "scullingRowerCount")
    @NonNull
    private Integer scullingRowerCount;
}
