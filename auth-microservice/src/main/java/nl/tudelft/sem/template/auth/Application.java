package nl.tudelft.sem.template.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main class of the Authentication microservice application.
 */
@SpringBootApplication
public class Application {

    /**
     * Starts the application.
     *
     * @param args The arguments provided when calling the program.
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
