package nl.tudelft.sem.template.matching.domain;

import nl.tudelft.sem.template.matching.authentication.AuthManager;
import nl.tudelft.sem.template.matching.domain.database.MatchingRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SanitizationTest {

    private Sanitization service;

    @Mock
    private AuthManager authManager;
    @Mock
    private MatchingRepo matchingRepo;

    Match match;

    @BeforeEach
    void setUp() {
        service = new Sanitization(authManager, matchingRepo);
        match = new Match("d.micloiu@tudelft.nl",
                2L,
                "l.tosa@tudelft.nl",
                "cox");
    }

    @Test
    void verifyMatchMatchIdNotPresent() {
        when(matchingRepo.getMatchByMatchId(1L)).thenReturn(Optional.empty());
        assertThat(service.verifyMatch(1L)).isFalse();
    }

    @Test
    void verifyMatchMaliciousUser() {
        when(matchingRepo.getMatchByMatchId(1L)).thenReturn(Optional.of(match));
        when(authManager.getUserId()).thenReturn("d.micloiu@icloud.nl");
        assertThat(service.verifyMatch(1L)).isFalse();
    }

    @Test
    void verifyMatchTrue() {
        when(matchingRepo.getMatchByMatchId(1L)).thenReturn(Optional.of(match));
        when(authManager.getUserId()).thenReturn("d.micloiu@tudelft.nl");
        assertThat(service.verifyMatch(1L)).isTrue();
    }

    @Test
    void verifyPosition() {
        assertThat(service.verifyPosition("cox")).isTrue();
        assertThat(service.verifyPosition("starboard")).isTrue();
        assertThat(service.verifyPosition("coach")).isTrue();
        assertThat(service.verifyPosition("port")).isTrue();
        assertThat(service.verifyPosition("sculling")).isTrue();

        assertThat(service.verifyPosition("Cox")).isFalse();
        assertThat(service.verifyPosition("random_position")).isFalse();
        assertThat(service.verifyPosition("coach.")).isFalse();

    }

    @Test
    void getMatches() {
        when(authManager.getUserId()).thenReturn("d.micloiu@tudelft.nl");
        when(matchingRepo.getMatchesByParticipantIdAndStatus("d.micloiu@tudelft.nl",
                Status.MATCHED)).thenReturn(List.of(match));

        assertThat(service.getMatches(Status.MATCHED)).isEqualTo(List.of(match));
    }
}