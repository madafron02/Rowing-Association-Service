package nl.tudelft.sem.template.matching.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Model representing a list of activity responses.
 */
@Data
@AllArgsConstructor
public class MatchingResponseModel {
    private List<ActivityReponse> activities;
}
