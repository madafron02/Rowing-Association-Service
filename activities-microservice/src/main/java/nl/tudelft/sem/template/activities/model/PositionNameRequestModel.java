package nl.tudelft.sem.template.activities.model;

import lombok.Data;

/**
 * Model representing a position name because otherwise the String might not be deserialised properly.
 */
@Data
public class PositionNameRequestModel {

    String position;
}
