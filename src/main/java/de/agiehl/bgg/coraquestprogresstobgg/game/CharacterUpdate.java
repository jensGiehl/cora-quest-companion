package de.agiehl.bgg.coraquestprogresstobgg.game;

public record CharacterUpdate(
        String playerName,
        String characterName,
        int health
) {}
