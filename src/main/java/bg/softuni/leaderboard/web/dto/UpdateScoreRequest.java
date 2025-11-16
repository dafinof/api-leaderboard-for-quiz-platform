package bg.softuni.leaderboard.web.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateScoreRequest {
    private int score;
}
