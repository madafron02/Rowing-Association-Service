package nl.tudelft.sem.template.notification.controllers;

import nl.tudelft.sem.template.notification.authentication.AuthManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello World example controller.
 * <p>
 * This controller shows how you can extract information from the JWT token.
 * </p>
 */
@RestController
public class NotificationController {

    private final transient AuthManager authManager;

    /**
     * Instantiates a new controller.
     *
     * @param authManager Spring Security component used to authenticate and authorize the user
     */
    @Autowired
    public NotificationController(AuthManager authManager) {
        this.authManager = authManager;
    }

    /**
     * Gets example by id.
     *
     * @return the example found in the database with the given id
     */
    @GetMapping("/hello")
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok("Hello " + authManager.getNetId() + ", this is the notification microservice.");

    }
}

