package bg.softuni.leaderboard.repository;

import bg.softuni.leaderboard.model.UserScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserScoreRepository extends JpaRepository<UserScore, UUID> {
    List<UserScore> findTop10ByOrderByScoreDesc();
}
