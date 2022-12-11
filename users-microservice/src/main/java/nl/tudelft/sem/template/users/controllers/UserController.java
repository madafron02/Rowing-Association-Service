package nl.tudelft.sem.template.users.controllers;

import nl.tudelft.sem.template.users.authentication.AuthManager;
import nl.tudelft.sem.template.users.domain.User;
import nl.tudelft.sem.template.users.domain.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;



/**
 * UserController for controlling requests for adding and receiving user data.
 *
 */
@RestController
@RequestMapping("user")
public class UserController {

    private final transient AuthManager authManager;
    private final transient UserService userService;

    /**
     * Instantiates a new controller.
     *
     * @param authManager Spring Security component used to authenticate and authorize the user
     * @param userService Service for handling user data
     */
    @Autowired
    public UserController(AuthManager authManager, UserService userService) {
        this.authManager = authManager;
        this.userService = userService;
    }

    /**
     * Gets data for the user that is currently logged in.
     *
     * @return User entity of authenticated client.
     */
    @GetMapping("/mydata")
    public ResponseEntity<User> getUserData() {
        User user = userService.getByEmail(authManager.getNetId());
        if (user == null) {
            return new ResponseEntity("User with the email: " + authManager.getNetId() + " was not found.",
                    HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(user);
    }

    /**
     * Gets data for the user specified by email.
     *
     * @return User entity of user specified by email.
     */
    @GetMapping("/details")
    public ResponseEntity<User> getUserDataById(@RequestBody String email) {
        User user = userService.getByEmail(email);
        if (user == null) {
            return new ResponseEntity("User with the email: " + email + " was not found.", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(user);
    }

    /**
     * Post mapping for creating a new user, specified by email.
     *
     * @param email email of new user
     * @return user entity created, or error if a user with that email already exists
     */
    @PostMapping("/newuser")
    public ResponseEntity<User> createNewUser(@RequestBody String email) {
        User newUser = userService.createUser(email);
        if (newUser == null) {
            return new ResponseEntity("User with the email:" + email + " already exists", HttpStatus.CONFLICT);
        }
        return ResponseEntity.ok(newUser);
    }
}
