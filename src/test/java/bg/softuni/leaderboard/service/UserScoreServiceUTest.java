package bg.softuni.leaderboard.service;

import bg.softuni.leaderboard.exception.ScoreNotFoundException;
import bg.softuni.leaderboard.model.UserScore;
import bg.softuni.leaderboard.repository.UserScoreRepository;
import bg.softuni.leaderboard.web.dto.CreateScoreRequest;
import bg.softuni.leaderboard.web.dto.UserScoreResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserScoreServiceUTest {
    @Mock
    private UserScoreRepository repository;

    @InjectMocks
    private UserScoreService service;

    @Test
    void upsertScore_whenNoExistingScore_createsNew() {
        UUID userId = UUID.randomUUID();

        CreateScoreRequest request = new CreateScoreRequest(
                userId,
                100,
                "PlayerOne",
                "avatar.png"
        );

        when(repository.findAll()).thenReturn(Collections.emptyList());

        ArgumentCaptor<UserScore> captured = ArgumentCaptor.forClass(UserScore.class);

        when(repository.save(captured.capture()))
                .thenAnswer(inv -> inv.getArgument(0));

        UserScoreResponse response = service.upsertScore(userId, request);

        UserScore saved = captured.getValue();
        assertEquals(userId, saved.getUserId());
        assertEquals("PlayerOne", saved.getUsername());
        assertEquals(100, saved.getScore());
        assertNotNull(saved.getUpdatedOn());

        assertEquals(100, response.getScore());
    }


    @Test
    void upsertScore_whenScoreExists_updatesExisting() {
        UUID userId = UUID.randomUUID();

        UserScore existing = UserScore.builder()
                .userId(userId)
                .username("Old")
                .avatarUrl("old.png")
                .score(50)
                .updatedOn(LocalDateTime.now().minusDays(2))
                .build();

        when(repository.findAll()).thenReturn(List.of(existing));

        CreateScoreRequest request = new CreateScoreRequest(
                userId,
                200,
                "Old",
                "old.png"
        );

        ArgumentCaptor<UserScore> captured = ArgumentCaptor.forClass(UserScore.class);

        when(repository.save(captured.capture()))
                .thenAnswer(inv -> inv.getArgument(0));

        UserScoreResponse response = service.upsertScore(userId, request);

        UserScore updated = captured.getValue();
        assertEquals(200, updated.getScore());
        assertNotNull(updated.getUpdatedOn());

        assertEquals(200, response.getScore());
    }

   @Test
    void deleteScore_whenExists_deletes() {
        UUID id = UUID.randomUUID();

        when(repository.existsById(id)).thenReturn(true);

        service.deleteScore(id);

        verify(repository).deleteById(id);
    }

    @Test
    void deleteScore_whenMissing_throws() {
        UUID id = UUID.randomUUID();

        when(repository.existsById(id)).thenReturn(false);

        assertThrows(ScoreNotFoundException.class, () -> service.deleteScore(id));
    }

    @Test
    void getTopScores_returnsMappedResponses() {
        UserScore s1 = UserScore.builder()
                .userId(UUID.randomUUID())
                .username("A")
                .score(300)
                .updatedOn(LocalDateTime.now())
                .build();

        UserScore s2 = UserScore.builder()
                .userId(UUID.randomUUID())
                .username("B")
                .score(150)
                .updatedOn(LocalDateTime.now())
                .build();

        when(repository.findTop10ByOrderByScoreDesc()).thenReturn(List.of(s1, s2));

        List<UserScoreResponse> result = service.getTopScores();

        assertEquals(2, result.size());
        assertEquals("A", result.get(0).getUsername());
        assertEquals(300, result.get(0).getScore());
    }

    @Test
    void deleteAllScores_whenHasScores_deletes() {
        when(repository.count()).thenReturn(5L);

        service.deleteAllScores();

        verify(repository).deleteAll();
    }

    @Test
    void deleteAllScores_whenEmpty_throws() {
        when(repository.count()).thenReturn(0L);

        assertThrows(
                ScoreNotFoundException.class,
                () -> service.deleteAllScores()
        );
    }
}
