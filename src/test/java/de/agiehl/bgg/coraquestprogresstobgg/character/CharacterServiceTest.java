package de.agiehl.bgg.coraquestprogresstobgg.character;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CharacterServiceTest {

    private CharacterService characterService;

    @BeforeEach
    void setUp() throws Exception {
        characterService = new CharacterService();
        characterService.loadCharacters();
    }

    @Test
    void loadsCharactersFromJson() {
        assertThat(characterService.findAll()).isNotEmpty();
    }

    @Test
    void allCharactersHaveNameAndSource() {
        characterService.findAll().forEach(character -> {
            assertThat(character.name()).isNotBlank();
            assertThat(character.source()).isNotNull();
        });
    }
}
