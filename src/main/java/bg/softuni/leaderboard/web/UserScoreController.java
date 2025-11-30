package bg.softuni.leaderboard.web;

import bg.softuni.leaderboard.service.UserScoreService;
import bg.softuni.leaderboard.web.dto.CreateScoreRequest;
import bg.softuni.leaderboard.web.dto.UserScoreResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/scores")
public class UserScoreController {

    private final UserScoreService userScoreService;

    public UserScoreController(UserScoreService userScoreService) {
        this.userScoreService = userScoreService;
    }

    @GetMapping("/v1/top")
    public ResponseEntity<List<UserScoreResponse>> topScores() {
        List<UserScoreResponse> topScores = userScoreService.getTopScores();
        return ResponseEntity.ok(topScores);
    }

    @PutMapping("/v1/{id}")
    public ResponseEntity<UserScoreResponse> upsertUserScore(@PathVariable UUID id, @RequestBody CreateScoreRequest request) {
        UserScoreResponse response = userScoreService.upsertScore(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/v1/{id}")
    public ResponseEntity<Void> deleteUserScore(@PathVariable UUID id) {
        userScoreService.deleteScore(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/v1")
    public ResponseEntity<Void> deleteAllScores() {
        userScoreService.deleteAllScores();
        return ResponseEntity.noContent().build();
    }
}
