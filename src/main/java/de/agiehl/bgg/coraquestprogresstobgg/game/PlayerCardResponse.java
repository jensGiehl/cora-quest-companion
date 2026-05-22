package de.agiehl.bgg.coraquestprogresstobgg.game;

public record PlayerCardResponse(
        Long id,
        String cardName,
        String cardSource,
        String cardType
) {}
