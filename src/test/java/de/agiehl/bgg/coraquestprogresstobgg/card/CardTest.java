package de.agiehl.bgg.coraquestprogresstobgg.card;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.agiehl.bgg.coraquestprogresstobgg.common.GameSource;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CardTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void deserializesItemCard() throws Exception {
        String json = """
                {"name": "Sword", "source": "BASE", "type": "ITEM"}
                """;

        Card card = objectMapper.readValue(json, Card.class);

        assertThat(card.name()).isEqualTo("Sword");
        assertThat(card.source()).isEqualTo(GameSource.BASE);
        assertThat(card.type()).isEqualTo(CardType.ITEM);
    }

    @Test
    void deserializesSpecialItemCard() throws Exception {
        String json = """
                {"name": "Magic Ring", "source": "BASE", "type": "SPECIAL_ITEM"}
                """;

        Card card = objectMapper.readValue(json, Card.class);

        assertThat(card.type()).isEqualTo(CardType.SPECIAL_ITEM);
    }

    @Test
    void deserializesLevelCardFromExpansion() throws Exception {
        String json = """
                {"name": "Level 5", "source": "KEEP_QUESTING", "type": "LEVEL_CARD"}
                """;

        Card card = objectMapper.readValue(json, Card.class);

        assertThat(card.source()).isEqualTo(GameSource.KEEP_QUESTING);
        assertThat(card.type()).isEqualTo(CardType.LEVEL_CARD);
    }
}
