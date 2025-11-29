package bg.softuni.leaderboard.job;

import bg.softuni.leaderboard.service.UserScoreService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LeaderboardResetter {

    private final UserScoreService userScoreService;

    public LeaderboardResetter(UserScoreService userScoreService) {
        this.userScoreService = userScoreService;
    }

    @Scheduled(cron = "0 0 0 * * 7")
    public void resetLeaderboard() {
        userScoreService.deleteAllScores();
    }
}
