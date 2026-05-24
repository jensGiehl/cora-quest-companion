package de.agiehl.bgg.coraquestprogresstobgg.game;

import java.util.List;

public record GameStateResponse(
        String questName,
        String difficulty,
        String gameLength,
        boolean secondWind,
        int gold,
        List<String> curses,
        List<PlayerCharacterResponse> characters
) {
}
