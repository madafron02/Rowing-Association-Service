package nl.tudelft.sem.template.matching.application;

import jakarta.ws.rs.client.ClientBuilder;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class UsersCommunication {

    private static final String SERVER = "http://localhost:8082/";

    public static String getUserDetails(String userId) {
        return ClientBuilder.newClient()
                .target(SERVER)
                .path("hello")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(String.class);
    }
}
