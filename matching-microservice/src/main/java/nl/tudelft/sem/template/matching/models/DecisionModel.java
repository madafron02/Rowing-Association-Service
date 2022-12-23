package nl.tudelft.sem.template.matching.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DecisionModel {
    private long matchId;
    private boolean decision;
}
