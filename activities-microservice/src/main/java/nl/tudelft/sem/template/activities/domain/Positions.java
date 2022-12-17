package nl.tudelft.sem.template.activities.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

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
}
