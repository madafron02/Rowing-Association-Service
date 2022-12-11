package nl.tudelft.sem.template.notification.controllers;

import nl.tudelft.sem.template.notification.authentication.AuthManager;
import nl.tudelft.sem.template.notification.builders.Builder;
import nl.tudelft.sem.template.notification.builders.NotificationBuilder;
import nl.tudelft.sem.template.notification.domain.Director;
import nl.tudelft.sem.template.notification.domain.Notification;
import nl.tudelft.sem.template.notification.models.NotificationRequestModelOwner;
import nl.tudelft.sem.template.notification.models.NotificationRequestModelParticipant;
import nl.tudelft.sem.template.notification.models.NotificationRequestModelParticipantChanges;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

/**
 * Notification microservice controller.
 *
 * This controller allows sending email notifications through API requests.
 *
 */
@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final transient AuthManager authManager;
    private Director director;

    @Autowired
    private JavaMailSender javaMailSender;

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

    @PostMapping(value = "/participant",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<String> sendNotificationToPlayer(
            @RequestBody NotificationRequestModelParticipant notificationRequestModelParticipant) {
        try {
            Builder builder = new NotificationBuilder();
            director = new Director(builder);
            director.makeNotificationForPlayer(notificationRequestModelParticipant.getParticipantId(),
                    notificationRequestModelParticipant.getActivityId(),
                    notificationRequestModelParticipant.getTimeslot(),
                    notificationRequestModelParticipant.isDecision());
            sendNotification(builder.build());
            return ResponseEntity.ok("Email sent successfully.");
        } catch (Exception exception) {
            if(exception instanceof MailException){
                return ResponseEntity.unprocessableEntity().body(exception.getMessage());
            }
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PostMapping(value = "/activity-changed",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<String> sendNotificationToPlayerChanges(
            @RequestBody NotificationRequestModelParticipantChanges notificationRequestModelParticipantChanges) {
        try {
            Builder builder = new NotificationBuilder();
            director = new Director(builder);
            director.makeNotificationForPlayerChanges(notificationRequestModelParticipantChanges.getParticipantId(),
                    notificationRequestModelParticipantChanges.getActivityId(),
                    notificationRequestModelParticipantChanges.getTimeslot());
            sendNotification(builder.build());
            return ResponseEntity.ok("Email sent successfully.");
        } catch (Exception exception) {
            if(exception instanceof MailException){
                return ResponseEntity.unprocessableEntity().body(exception.getMessage());
            }
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PostMapping(value = "/publisher",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<String> sendNotificationToPublisher(
            @RequestBody NotificationRequestModelOwner notificationRequestModelOwner) {
        try {
            Builder builder = new NotificationBuilder();
            director = new Director(builder);
            director.makeNotificationForPublisher(notificationRequestModelOwner.getOwnerId(),
                    notificationRequestModelOwner.getParticipantId(),
                    notificationRequestModelOwner.getActivityId(),
                    notificationRequestModelOwner.getTimeslot());
            sendNotification(builder.build());
            return ResponseEntity.ok("Email sent successfully.");
        } catch (MailException mailException) {
            return ResponseEntity.unprocessableEntity().body(mailException.getMessage());
        }
    }

    private void sendNotification(Notification notification) throws MailException {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(notification.getReceiverEmail());
        mail.setSubject("New notification regarding rowing competitions");
        mail.setText(notification.getMessage());
        javaMailSender.send(mail);
    }
}

