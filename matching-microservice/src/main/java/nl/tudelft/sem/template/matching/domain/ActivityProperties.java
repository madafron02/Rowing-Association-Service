package nl.tudelft.sem.template.matching.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ActivityProperties {
    private String gender;
    private String organisation;
    private boolean competition;
    private String certificate;
}
