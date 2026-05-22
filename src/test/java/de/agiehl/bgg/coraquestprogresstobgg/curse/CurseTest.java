package de.agiehl.bgg.coraquestprogresstobgg.curse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CurseTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void deserializesCurse() throws Exception {
        String json = """
                {"description": "You lose 1 health point"}
                """;

        Curse curse = objectMapper.readValue(json, Curse.class);

        assertThat(curse.description()).isEqualTo("You lose 1 health point");
    }
}
