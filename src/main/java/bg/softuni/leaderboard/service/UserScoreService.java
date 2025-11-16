package bg.softuni.leaderboard.service;

import bg.softuni.leaderboard.model.UserScore;
import bg.softuni.leaderboard.repository.UserScoreRepository;
import bg.softuni.leaderboard.web.dto.CreateScoreRequest;
import bg.softuni.leaderboard.web.dto.DtoMapperUserScore;
import bg.softuni.leaderboard.web.dto.UpdateScoreRequest;
import bg.softuni.leaderboard.web.dto.UserScoreResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserScoreService {


    private final UserScoreRepository repository;

    public UserScoreService(UserScoreRepository repository) {
        this.repository = repository;
    }

    public UserScoreResponse createScore(CreateScoreRequest request) {
        UserScore userScore = UserScore.builder()
                .userId(request.getUserId())
                .username(request.getUsername())
                .score(request.getScore())
                .updatedOn(LocalDateTime.now())
                .build();

        repository.save(userScore);

        return DtoMapperUserScore.fromUserScoreToUserScoreResponse(userScore);
    }

    public UserScoreResponse updateScore(UUID id, UpdateScoreRequest request) {
        UserScore userScore = repository.findById(id).orElse(null);
        if (userScore == null) {
            throw new RuntimeException("User id not found");
        }

        userScore.setScore(userScore.getScore() + request.getScore());
        userScore.setUpdatedOn(LocalDateTime.now());
        userScore = repository.save(userScore);

        return DtoMapperUserScore.fromUserScoreToUserScoreResponse(userScore);
    }

    public void deleteScore(UUID id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("User id not found");
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
        repository.deleteAll();
    }
}
