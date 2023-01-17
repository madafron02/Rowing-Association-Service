package nl.tudelft.sem.template.matching.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ActivityInformation {

    @Column(name = "acivity_id", nullable = false)
    private long activityId;

    @Column(name = "owner_id", nullable = false)
    private String ownerId;

    @Column(name = "position", nullable = false)
    private String position;


}
