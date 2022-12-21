package nl.tudelft.sem.template.users.controllers;

import nl.tudelft.sem.template.users.authentication.AuthManager;
import nl.tudelft.sem.template.users.domain.EmailAlreadyInUseException;
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
        User user = userService.getByEmail(authManager.getUserId());
        if (user == null) {
            return new ResponseEntity("User with the email: " + authManager.getUserId() + " was not found.",
                    HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(user);
    }

    /**
     * Gets data for the user specified by email.
     *
     * @return User entity of user specified by email.
     */
    @PostMapping("/details")
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
     * @param user new user entity
     * @return user entity created, or error if a user with that email already exists or email is invalid
     */
    @PostMapping("/newuser")
    public ResponseEntity<User> createNewUser(@RequestBody User user)
            throws Exception {

        if (!user.getEmail().equals(authManager.getUserId())) {
            return new ResponseEntity("You are not authenticated with the email: " + user.getEmail(), HttpStatus.CONFLICT);
        }
        try {
            User newUser = userService.saveUser(user);
            return ResponseEntity.ok(newUser);
        } catch (EmailAlreadyInUseException e) {
            return new ResponseEntity("User with the email:" + user.getEmail() + " already exists.", HttpStatus.CONFLICT);
        }
    }

    /**
     * Post mapping for updating a users data, specified by email.
     *
     * @param user user entity with updated data
     * @return updated user entity, or error if a user with that email doesn't exist
     */
    @PostMapping("/updatemydata")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        if (!user.getEmail().equals(authManager.getUserId())) {
            return new ResponseEntity("You are not authenticated with the email: " + user.getEmail(), HttpStatus.CONFLICT);
        } else if (!userService.userExists(user.getEmail())) {
            return new ResponseEntity("This user does not exist, please create a user account first.", HttpStatus.CONFLICT);
        }
        return ResponseEntity.ok(userService.updateUser(user));
    }
}
