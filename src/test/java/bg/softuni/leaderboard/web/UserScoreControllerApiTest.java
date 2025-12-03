package bg.softuni.leaderboard.web;

import bg.softuni.leaderboard.exception.ScoreNotFoundException;
import bg.softuni.leaderboard.service.UserScoreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserScoreController.class)
public class UserScoreControllerApiTest {
    @MockitoBean
    UserScoreService userScoreService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deleteUserScore_ShouldReturn204() throws Exception {
        UUID scoreId = UUID.randomUUID();
        doNothing().when(userScoreService).deleteScore(scoreId);

        mockMvc.perform(delete("/api/scores/v1/{id}", scoreId)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(userScoreService, times(1)).deleteScore(scoreId);
    }

    @Test
    void deleteUserScore_WhenNotFound_ShouldReturn404() throws Exception {
        UUID scoreId = UUID.randomUUID();
        doThrow(new ScoreNotFoundException(scoreId))
                .when(userScoreService).deleteScore(scoreId);

        mockMvc.perform(delete("/api/scores/v1/{id}", scoreId)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(userScoreService, times(1)).deleteScore(scoreId);
    }

    @Test
    void deleteAllScores_ShouldReturn204() throws Exception {
        doNothing().when(userScoreService).deleteAllScores();

        mockMvc.perform(delete("/api/scores/v1")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(userScoreService, times(1)).deleteAllScores();
    }

    @Test
    void deleteAllScores_WhenNoScoresExist_ShouldReturn404() throws Exception {
        doThrow(new ScoreNotFoundException("No scores available to delete"))
                .when(userScoreService).deleteAllScores();

        mockMvc.perform(delete("/api/scores/v1")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(userScoreService, times(1)).deleteAllScores();
    }

}
