package de.agiehl.bgg.coraquestprogresstobgg.game;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GameCurseRepository extends CrudRepository<GameCurse, Long> {

    List<GameCurse> findByGameId(Long gameId);

    @Modifying
    @Query("DELETE FROM game_curse WHERE game_id = :gameId")
    void deleteByGameId(Long gameId);
}
