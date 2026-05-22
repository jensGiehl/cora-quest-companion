package de.agiehl.bgg.coraquestprogresstobgg.game;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GameRepository extends CrudRepository<Game, Long> {

    Optional<Game> findByCode(String code);

    boolean existsByCode(String code);
}
