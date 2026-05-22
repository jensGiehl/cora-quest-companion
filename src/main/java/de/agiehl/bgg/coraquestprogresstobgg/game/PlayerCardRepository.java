package de.agiehl.bgg.coraquestprogresstobgg.game;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PlayerCardRepository extends CrudRepository<PlayerCard, Long> {

    List<PlayerCard> findByCharacterId(Long characterId);

    @Modifying
    @Query("DELETE FROM player_card WHERE character_id = :characterId")
    void deleteByCharacterId(Long characterId);
}
