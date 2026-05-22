package de.agiehl.bgg.coraquestprogresstobgg.character;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.agiehl.bgg.coraquestprogresstobgg.common.GameSource;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GameCharacterTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void deserializesBaseCharacter() throws Exception {
        String json = """
                {"name": "Cora", "source": "BASE"}
                """;

        GameCharacter character = objectMapper.readValue(json, GameCharacter.class);

        assertThat(character.name()).isEqualTo("Cora");
        assertThat(character.source()).isEqualTo(GameSource.BASE);
    }

    @Test
    void deserializesExpansionCharacter() throws Exception {
        String json = """
                {"name": "Hero", "source": "KEEP_QUESTING"}
                """;

        GameCharacter character = objectMapper.readValue(json, GameCharacter.class);

        assertThat(character.name()).isEqualTo("Hero");
        assertThat(character.source()).isEqualTo(GameSource.KEEP_QUESTING);
    }
}
