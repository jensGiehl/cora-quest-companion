package de.agiehl.bgg.coraquestprogresstobgg.game;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PlayerCharacterRepository extends CrudRepository<PlayerCharacter, Long> {

    List<PlayerCharacter> findByGameId(Long gameId);
}
