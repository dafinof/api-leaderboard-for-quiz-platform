package bg.softuni.leaderboard.web.dto;

import bg.softuni.leaderboard.model.UserScore;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapperUserScore {
    public static UserScoreResponse fromUserScoreToUserScoreResponse(UserScore userScore) {
        return UserScoreResponse.builder()
                .id(userScore.getId())
                .userId(userScore.getUserId())
                .score(userScore.getScore())
                .updatedOn(userScore.getUpdatedOn())
                .build();
    }
}
