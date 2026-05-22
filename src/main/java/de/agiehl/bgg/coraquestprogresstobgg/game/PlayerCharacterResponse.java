package de.agiehl.bgg.coraquestprogresstobgg.game;

import java.util.List;

public record PlayerCharacterResponse(
        Long id,
        String playerName,
        String characterName,
        int health,
        List<PlayerCardResponse> cards
) {}
