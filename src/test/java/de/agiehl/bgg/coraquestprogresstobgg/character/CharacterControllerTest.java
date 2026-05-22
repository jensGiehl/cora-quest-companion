package de.agiehl.bgg.coraquestprogresstobgg.character;

import de.agiehl.bgg.coraquestprogresstobgg.common.GameSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CharacterController.class)
class CharacterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CharacterService characterService;

    @Test
    void returnsCharactersAsJson() throws Exception {
        when(characterService.findAll()).thenReturn(List.of(
                new GameCharacter("Cora", GameSource.BASE),
                new GameCharacter("Hero", GameSource.KEEP_QUESTING)
        ));

        mockMvc.perform(get("/api/characters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Cora"))
                .andExpect(jsonPath("$[0].source").value("BASE"))
                .andExpect(jsonPath("$[1].source").value("KEEP_QUESTING"));
    }
}
