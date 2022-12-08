package nl.tudelft.sem.template.matching.models;

import lombok.Data;

import java.util.List;

/**
 * Model representing a list of activity responses.
 */
@Data
public class MatchingResponseModel {
    private List<ActivityReponse> activities;
}
