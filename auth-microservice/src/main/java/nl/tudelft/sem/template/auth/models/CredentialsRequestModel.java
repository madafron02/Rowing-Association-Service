package nl.tudelft.sem.template.auth.models;

import lombok.Data;

/**
 * Model representing a request with only userId and password.
 * To be used for both registration and authentication requests.
 */
@Data
public class CredentialsRequestModel {
    private String userId;
    private String password;
}
