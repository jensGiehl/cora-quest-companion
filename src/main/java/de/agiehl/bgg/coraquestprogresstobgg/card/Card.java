package de.agiehl.bgg.coraquestprogresstobgg.card;

import de.agiehl.bgg.coraquestprogresstobgg.common.GameSource;

public record Card(String name, GameSource source, CardType type) {
}
