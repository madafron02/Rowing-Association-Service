package nl.tudelft.sem.template.notification.controllers;

import nl.tudelft.sem.template.notification.authentication.AuthManager;
import nl.tudelft.sem.template.notification.builders.Builder;
import nl.tudelft.sem.template.notification.builders.NotificationBuilder;
import nl.tudelft.sem.template.notification.domain.Director;
import nl.tudelft.sem.template.notification.domain.Notification;
import nl.tudelft.sem.template.notification.models.NotificationRequestModelOwner;
import nl.tudelft.sem.template.notification.models.NotificationRequestModelParticipant;
import nl.tudelft.sem.template.notification.models.NotificationRequestModelParticipantChanges;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Notification microservice controller.
 * This controller allows sending email notifications through API requests.
 */
@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final transient AuthManager authManager;
    protected transient Director director;

    @Autowired
    private transient JavaMailSender javaMailSender;

    /**
     * Instantiates a new notification controller.
     *
     * @param authManager Spring Security component used to authenticate and authorize the user
     * @param director to direct the building process
     */
    @Autowired
    public NotificationController(AuthManager authManager, Director director) {
        this.authManager = authManager;
        this.director = director;
    }

    /**
     * Gets example by id.
     *
     * @return the example found in the database with the given id
     */
    @GetMapping("/hello")
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok("Hello " + authManager.getUserId() + ", this is the notification microservice.");
    }

    /**
     * Sends a notification through email to a player when he receives a decision from the owner of
     * the activity he signed up for.
     *
     * @param notificationRequestModelParticipant the request bodu format
     * @return if email is sent successfully returns 200 OK,
     *          otherwise 422 Unprocessable Entity and the exception message
     */
    @PostMapping(value = "/participant",
                consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
                produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<String> sendNotificationToPlayer(
                @RequestBody NotificationRequestModelParticipant notificationRequestModelParticipant) {
        try {
            Builder builder = new NotificationBuilder();
            director.setBuilder(builder);
            director.makeNotificationForPlayer(notificationRequestModelParticipant.getParticipantId(),
                    notificationRequestModelParticipant.getActivityId(),
                    notificationRequestModelParticipant.getTimeslot(),
                    notificationRequestModelParticipant.isDecision());
            sendNotification(builder.build());
            return ResponseEntity.ok("Email sent successfully.");
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    /**
     * Sends a notification through email to a player when the activity he was approved for
     * gets deleted or if its details are changed.
     *
     * @param notificationRequestModelParticipantChanges the request body format
     * @return if email is sent successfully returns 200 OK,
     *      otherwise 422 Unprocessable Entity and the exception message
     */
    @PostMapping(value = "/activity-changed",
                consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
                produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<String> sendNotificationToPlayerChanges(
                @RequestBody NotificationRequestModelParticipantChanges notificationRequestModelParticipantChanges) {
        try {
            Builder builder = new NotificationBuilder();
            director.setBuilder(builder);
            director.makeNotificationForPlayerChanges(notificationRequestModelParticipantChanges.getParticipantId(),
                    notificationRequestModelParticipantChanges.getActivityId(),
                    notificationRequestModelParticipantChanges.getTimeslot());
            sendNotification(builder.build());
            return ResponseEntity.ok("Email sent successfully.");
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    /**
     * Sends email to publisher of an activity when a user wants to sign up for it in a certain timeslot.
     *
     * @param notificationRequestModelOwner request body format
     * @return if email is sent successfully returns 200 OK,
     *      otherwise 422 Unprocessable Entity and the exception message
     */
    @PostMapping(value = "/publisher",
                consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
                produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<String> sendNotificationToPublisher(
                @RequestBody NotificationRequestModelOwner notificationRequestModelOwner) {
        try {
            Builder builder = new NotificationBuilder();
            director.setBuilder(builder);
            director.makeNotificationForPublisher(notificationRequestModelOwner.getOwnerId(),
                    notificationRequestModelOwner.getParticipantId(),
                    notificationRequestModelOwner.getActivityId(),
                    notificationRequestModelOwner.getTimeslot());
            sendNotification(builder.build());
            return ResponseEntity.ok("Email sent successfully.");
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    /**
     * Sends the notification using the JavaMailSender.
     *
     * @param notification notification to be sent
     * @throws MailException if the notification cannot be sent
     */
    public void sendNotification(Notification notification) throws MailException {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(notification.getReceiverEmail());
        mail.setSubject("New notification regarding rowing competitions");
        mail.setText(notification.getMessage());
        javaMailSender.send(mail);
    }
}

