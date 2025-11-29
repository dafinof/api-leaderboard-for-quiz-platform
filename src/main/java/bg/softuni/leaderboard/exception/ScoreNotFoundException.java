package bg.softuni.leaderboard.exception;

import java.util.UUID;

public class ScoreNotFoundException extends RuntimeException {
    public ScoreNotFoundException(String message) {
        super(message);
    }

    public ScoreNotFoundException(UUID id) {
        super(id + " not found");
    }
}
