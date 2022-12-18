package nl.tudelft.sem.template.notification.controllers;

import nl.tudelft.sem.template.notification.authentication.AuthManager;
import nl.tudelft.sem.template.notification.domain.Timeslot;
import nl.tudelft.sem.template.notification.models.NotificationRequestModelOwner;
import nl.tudelft.sem.template.notification.models.NotificationRequestModelParticipant;
import nl.tudelft.sem.template.notification.models.NotificationRequestModelParticipantChanges;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
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
        notificationController = new NotificationController(authManager);
        this.mockMvc = MockMvcBuilders.standaloneSetup(notificationController).build();
    }

    @Test
    public void constructorTest() {
        assertThat(notificationController).isNotNull();
    }

    @Test
    public void helloWorldTest() throws Exception {
        when(authManager.getNetId()).thenReturn("test");
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

        try {
            ResponseEntity response = notificationController
                    .sendNotificationToPlayer(notificationRequestModelParticipant);
            assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        } catch (Exception e) {
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

        try {
            ResponseEntity response = notificationController
                    .sendNotificationToPlayerChanges(notificationRequestModelParticipantChanges);
            assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        } catch (Exception e) {
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

        try {
            ResponseEntity response = notificationController
                    .sendNotificationToPublisher(notificationRequestModelOwner);
            assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        } catch (Exception e) {
            assertThat(e).isNotNull();
        }
    }
}
