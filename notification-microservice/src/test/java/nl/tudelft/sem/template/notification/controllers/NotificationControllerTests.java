package nl.tudelft.sem.template.notification.controllers;

import nl.tudelft.sem.template.notification.authentication.AuthManager;
import nl.tudelft.sem.template.notification.domain.Notification;
import nl.tudelft.sem.template.notification.domain.Timeslot;
import nl.tudelft.sem.template.notification.models.NotificationRequestModelOwner;
import nl.tudelft.sem.template.notification.models.NotificationRequestModelParticipant;
import nl.tudelft.sem.template.notification.models.NotificationRequestModelParticipantChanges;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for NotificationController class.
 */
public class NotificationControllerTests {
    private transient NotificationController notificationController;
    private transient AuthManager authManager;
    private transient JavaMailSender javaMailSender;

    private transient NotificationRequestModelParticipant notificationRequestModelParticipant;
    private transient NotificationRequestModelParticipantChanges notificationRequestModelParticipantChanges;
    private transient NotificationRequestModelOwner notificationRequestModelOwner;

    private transient MockMvc mockMvc;
    private transient String random = "random";

    /**
     * General setup for tests.
     */
    @BeforeEach
    public void setup() {
        authManager = Mockito.mock(AuthManager.class);
        javaMailSender = Mockito.mock(JavaMailSender.class);
        doNothing().when(javaMailSender).send(isA(SimpleMailMessage.class));
        notificationController = new NotificationController(authManager);
        Field javaMail = ReflectionUtils.findField(NotificationController.class, "javaMailSender");
        ReflectionUtils.makeAccessible(javaMail);
        ReflectionUtils.setField(javaMail, notificationController, javaMailSender);
        this.mockMvc = MockMvcBuilders.standaloneSetup(notificationController).build();
    }

    @Test
    public void constructorTest() {
        assertThat(notificationController).isNotNull();
    }

    @Test
    public void helloWorldTest() throws Exception {
        when(authManager.getUserId()).thenReturn("test");
        this.mockMvc.perform(get("/notification/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello test, "
                        + "this is the notification microservice."));
    }

    @Test
    public void sendNotificationToPlayerOkTest() {
        notificationRequestModelParticipant = new NotificationRequestModelParticipant(
                "test@gmail.com", 999, new Timeslot(
                        LocalDateTime.now(), LocalDateTime.now().plusHours(1)), true);

        NotificationController spy = Mockito.spy(notificationController);
        doNothing().when(spy).sendNotification(any());

        ResponseEntity response = spy.sendNotificationToPlayer(notificationRequestModelParticipant);
        assertThat(response).isEqualTo(ResponseEntity.ok("Email sent successfully."));
    }

    @Test
    public void sendNotificationToPlayerBadRequestTest() {
        notificationRequestModelParticipant = new NotificationRequestModelParticipant(
                random, 999, new Timeslot(
                LocalDateTime.now(), LocalDateTime.now().plusHours(1)), true);
        doThrow(new RuntimeException()).when(javaMailSender).send(isA(SimpleMailMessage.class));
        try {
            ResponseEntity response = notificationController
                    .sendNotificationToPlayer(notificationRequestModelParticipant);
            assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        } catch (RuntimeException e) {
            assertThat(e).isNotNull();
        }
    }

    @Test
    public void sendNotificationToPlayerChangesOkTest() {
        notificationRequestModelParticipantChanges = new NotificationRequestModelParticipantChanges(
                "test@gmail.com", 999, new Timeslot(
                LocalDateTime.now(), LocalDateTime.now().plusHours(1)));

        NotificationController spy = Mockito.spy(notificationController);
        doNothing().when(spy).sendNotification(any());

        ResponseEntity response = spy.sendNotificationToPlayerChanges(notificationRequestModelParticipantChanges);
        assertThat(response).isEqualTo(ResponseEntity.ok("Email sent successfully."));
    }

    @Test
    public void sendNotificationToPlayerChangesBadRequestTest() {
        notificationRequestModelParticipantChanges = new NotificationRequestModelParticipantChanges(
                random, 999, new Timeslot(
                LocalDateTime.now(), LocalDateTime.now().plusHours(1)));
        doThrow(new RuntimeException()).when(javaMailSender).send(isA(SimpleMailMessage.class));
        try {
            ResponseEntity response = notificationController
                    .sendNotificationToPlayerChanges(notificationRequestModelParticipantChanges);
            assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        } catch (RuntimeException e) {
            assertThat(e).isNotNull();
        }
    }

    @Test
    public void sendNotificationToOwnerOkTest() {
        notificationRequestModelOwner = new NotificationRequestModelOwner(
                "test@gmail.com", "test1@gmail.com", 999, new Timeslot(
                LocalDateTime.now(), LocalDateTime.now().plusHours(1)));

        NotificationController spy = Mockito.spy(notificationController);
        doNothing().when(spy).sendNotification(any());

        ResponseEntity response = spy.sendNotificationToPublisher(notificationRequestModelOwner);
        assertThat(response).isEqualTo(ResponseEntity.ok("Email sent successfully."));
    }

    @Test
    public void sendNotificationToOwnerBadRequestTest() {
        notificationRequestModelOwner = new NotificationRequestModelOwner(
                random, random, 999, new Timeslot(
                LocalDateTime.now(), LocalDateTime.now().plusHours(1)));
        doThrow(new RuntimeException()).when(javaMailSender).send(isA(SimpleMailMessage.class));
        try {
            ResponseEntity response = notificationController
                    .sendNotificationToPublisher(notificationRequestModelOwner);
            assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        } catch (RuntimeException e) {
            assertThat(e).isNotNull();
        }
    }

    @Test
    public void sendNotificationTest() {
        Notification notification = new Notification("email", "message");
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(notification.getReceiverEmail());
        mail.setSubject("New notification regarding rowing competitions");
        mail.setText(notification.getMessage());

        notificationController.sendNotification(notification);
        verify(javaMailSender).send(mail);
    }
}
