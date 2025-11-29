package bg.softuni.leaderboard.service;

import bg.softuni.leaderboard.model.UserScore;
import bg.softuni.leaderboard.repository.UserScoreRepository;
import bg.softuni.leaderboard.web.dto.CreateScoreRequest;
import bg.softuni.leaderboard.web.dto.DtoMapperUserScore;
import bg.softuni.leaderboard.exception.ScoreNotFoundException;
import bg.softuni.leaderboard.web.dto.UserScoreResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserScoreService {

    private static final Logger logger = LoggerFactory.getLogger(UserScoreService.class);

    private final UserScoreRepository repository;

    public UserScoreService(UserScoreRepository repository) {
        this.repository = repository;
    }

    public UserScoreResponse upsertScore(UUID id, CreateScoreRequest request) {
        logger.info("Received request to upsert score for userId={}", id);

        UserScore userScore = null;

        List<UserScore> allScores = repository.findAll();
        for (int i = 0; i < allScores.size(); i++) {
            if (allScores.get(i).getUserId().equals(id)) {
                userScore = allScores.get(i);
                break;
            }
        }

        if (userScore == null) {
            logger.info("No score found for userId={}, creating new score entry", id);

            userScore = UserScore.builder()
                    .userId(request.getUserId())
                    .username(request.getUsername())
                    .avatarUrl(request.getAvatarUrl())
                    .score(request.getScore())
                    .updatedOn(LocalDateTime.now())
                    .build();
        } else {
            logger.info("Updating existing score for userId={} to new score={}", id, request.getScore());

            userScore.setScore(request.getScore());
            userScore.setUpdatedOn(LocalDateTime.now());
        }

        userScore = repository.save(userScore);

        return DtoMapperUserScore.fromUserScoreToUserScoreResponse(userScore);
    }

    public void deleteScore(UUID id) {
        if (!repository.existsById(id)) {
            throw new ScoreNotFoundException(id);
        }
        repository.deleteById(id);
    }

    public List<UserScoreResponse> getTopScores() {
        List<UserScore> topScores = repository.findTop10ByOrderByScoreDesc();
        return topScores.stream()
                .map(DtoMapperUserScore::fromUserScoreToUserScoreResponse)
                .collect(Collectors.toList());
    }

    public void deleteAllScores() {
        long count = repository.count();

        if (count == 0) {
            throw new ScoreNotFoundException("No scores available to delete");
        }

        repository.deleteAll();
    }
}
