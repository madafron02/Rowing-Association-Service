package nl.tudelft.sem.template.matching.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.tudelft.sem.template.matching.domain.Certificate;
import nl.tudelft.sem.template.matching.domain.Match;
import nl.tudelft.sem.template.matching.domain.MatchingService;
import nl.tudelft.sem.template.matching.domain.Sanitization;
import nl.tudelft.sem.template.matching.domain.TimeslotApp;
import nl.tudelft.sem.template.matching.domain.TypeOfActivity;
import nl.tudelft.sem.template.matching.domain.database.CertificateRepo;
import nl.tudelft.sem.template.matching.models.ActivityResponse;
import nl.tudelft.sem.template.matching.models.DecisionModel;
import nl.tudelft.sem.template.matching.models.MatchingRequestModel;
import nl.tudelft.sem.template.matching.models.MatchingResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MatchingControllerTest {

    @Mock
    private transient MatchingService service;

    @Mock
    private transient CertificateRepo repo;

    @Mock
    private transient Sanitization sanitizationService;

    private transient MatchingController controller;
    private transient MockMvc mockMvc;
    private transient ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        controller = new MatchingController(service, repo, sanitizationService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testSubmit() throws Exception {
        TimeslotApp availability = new TimeslotApp(LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        TimeslotApp timeslot = new TimeslotApp(LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3));
        MatchingResponseModel matchingResponseModel = new MatchingResponseModel(
                List.of(new ActivityResponse(1, TypeOfActivity.TRAINING, timeslot)));

        when(service.submitAvailability(availability, "cox")).thenReturn(matchingResponseModel);
        when(sanitizationService.verifyPosition("cox")).thenReturn(true);

        String response = mockMvc.perform(post("/matching/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new MatchingRequestModel(availability, "cox"))))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(response).isEqualTo(objectMapper.writeValueAsString(matchingResponseModel));
    }

    @Test
    void testSubmitBadRequest() throws Exception {
        TimeslotApp availability = new TimeslotApp(LocalDateTime.now(), LocalDateTime.now().plusDays(1));

        when(sanitizationService.verifyPosition(any())).thenReturn(false);

        mockMvc.perform(post("/matching/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new MatchingRequestModel(availability, "cox"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPick() throws Exception {
        when(sanitizationService.verifyMatch(1L)).thenReturn(true);

        String response = mockMvc.perform(post("/matching/pick")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(1L)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(response).isEqualTo("Application sent");
    }

    @Test
    void testPickBadRequest() throws Exception {
        when(sanitizationService.verifyMatch(1L)).thenReturn(false);

        mockMvc.perform(post("/matching/pick")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(1L)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDecide() throws Exception {
        DecisionModel decisionModel = new DecisionModel(1L, true);

        when(service.acceptOrDenyRequest(anyLong(), anyBoolean())).thenReturn(true);

        String response = mockMvc.perform(post("/matching/decide")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(decisionModel)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(response).isEqualTo("Notification sent successfully !");
    }

    @Test
    void testDecideBadRequest() throws Exception {
        DecisionModel decisionModel = new DecisionModel(1L, true);

        when(service.acceptOrDenyRequest(anyLong(), anyBoolean())).thenReturn(false);

        mockMvc.perform(post("/matching/decide")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(decisionModel)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPending() throws Exception {
        List<Match> matches = List.of(
                new Match("participant1@tudelft.nl", 1L, "l.tosa@student.tudelft.nl", "cox"),
                new Match("participant2@tudelft.nl", 1L, "l.tosa@student.tudelft.nl", "coach"),
                new Match("participant3@tudelft.nl", 2L, "l.tosa@student.tudelft.nl", "cox")
                );
        when(sanitizationService.getPendingRequests()).thenReturn(matches);

        String response = mockMvc.perform(get("/matching/participants"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(response).isEqualTo(objectMapper.writeValueAsString(matches));
    }

    @Test
    void testApplications() throws Exception {
        List<Match> matches = List.of(
                new Match("participant1@tudelft.nl", 1L, "l.tosa@student.tudelft.nl", "cox"),
                new Match("participant2@tudelft.nl", 1L, "l.tosa@student.tudelft.nl", "coach"),
                new Match("participant3@tudelft.nl", 2L, "l.tosa@student.tudelft.nl", "cox")
        );
        when(sanitizationService.getMatches(any())).thenReturn(matches);

        String response = mockMvc.perform(get("/matching/match/MATCHED"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(response).isEqualTo(objectMapper.writeValueAsString(matches));
    }

    @Test
    void testApplicationsBadRequest() throws Exception {
        String response = mockMvc.perform(get("/matching/match/NOTEXISTINGSTATUS"))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        assertThat(response).isEqualTo("Use a valid status (MATCHED, PENDING, ACCEPTED, DECLINED");
    }

    @Test
    void testActivityModified() throws Exception {
        String response = mockMvc.perform(post("/matching/activity/modified")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(1L)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(response).isEqualTo("Participants notified successfully !");
    }

    @Test
    void testValidateCertificateTrue() throws Exception {
        when(repo.getCertificateByName(any())).thenReturn(Optional.of(new Certificate()));
        String response = mockMvc.perform(post("/matching/certificate/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString("C4")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(response).isEqualTo(objectMapper.writeValueAsString(true));

    }

    @Test
    void testValidateCertificateFalse() throws Exception {
        when(repo.getCertificateByName(any())).thenReturn(Optional.empty());
        String response = mockMvc.perform(post("/matching/certificate/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString("D4")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(response).isEqualTo(objectMapper.writeValueAsString(false));
    }

    @Test
    void testAddCertificate() throws Exception {
        String certificate = "D4";
        when(repo.getCertificateByName(any())).thenReturn(Optional.empty());
        String response = mockMvc.perform(post("/matching/certificate/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(certificate))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        verify(repo, times(1)).getCertificateByName(certificate);
        verify(repo, times(1)).save(any(Certificate.class));
        assertThat(response).isEqualTo("Certificate successfully added!");
    }

    @Test
    void testAddCertificateExisted() throws Exception {
        String certificate = "D4";
        when(repo.getCertificateByName(any())).thenReturn(Optional.of(new Certificate(certificate)));
        String response = mockMvc.perform(post("/matching/certificate/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(certificate))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        verify(repo, times(1)).getCertificateByName(certificate);
        verify(repo, times(0)).save(any(Certificate.class));
        assertThat(response).isEqualTo("Certificate already added!");
    }
}