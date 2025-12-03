package bg.softuni.leaderboard;

import bg.softuni.leaderboard.model.UserScore;
import bg.softuni.leaderboard.repository.UserScoreRepository;
import bg.softuni.leaderboard.service.UserScoreService;
import bg.softuni.leaderboard.web.dto.UserScoreResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional

public class LeaderboardServiceITests {
    @Autowired
    private UserScoreService userScoreService;

    @Autowired
    private UserScoreRepository repository;

    @BeforeEach
    void cleanDB() {
        repository.deleteAll();
    }

    @Test
    void getTopScores_shouldReturnTop10OrderedByScoreDesc() {
        for (int i = 1; i <= 12; i++) {
            UserScore userScore = UserScore.builder()
                    .userId(UUID.randomUUID())
                    .username("User " + i)
                    .avatarUrl("avatar-" + i)
                    .score(i * 10)
                    .updatedOn(LocalDateTime.now())
                    .build();
            repository.save(userScore);
        }

        List<UserScoreResponse> result = userScoreService.getTopScores();

        assertThat(result).hasSize(10);

        assertThat(result)
                .extracting(UserScoreResponse::getScore)
                .isSortedAccordingTo((a, b) -> b - a)
                .containsExactly(120, 110, 100, 90, 80, 70, 60, 50, 40, 30);

        UserScoreResponse top = result.get(0);
        assertThat(top.getUsername()).isEqualTo("User 12");
        assertThat(top.getAvatarUrl()).isEqualTo("avatar-12");
        assertThat(top.getScore()).isEqualTo(120);
    }
}
