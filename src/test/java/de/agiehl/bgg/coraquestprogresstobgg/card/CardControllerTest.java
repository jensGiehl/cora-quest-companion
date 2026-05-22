package de.agiehl.bgg.coraquestprogresstobgg.card;

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

@WebMvcTest(CardController.class)
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CardService cardService;

    @Test
    void returnsCardsAsJson() throws Exception {
        when(cardService.findAll()).thenReturn(List.of(
                new Card("Sword", GameSource.BASE, CardType.ITEM),
                new Card("Level 5", GameSource.KEEP_QUESTING, CardType.LEVEL_CARD)
        ));

        mockMvc.perform(get("/api/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Sword"))
                .andExpect(jsonPath("$[0].source").value("BASE"))
                .andExpect(jsonPath("$[0].type").value("ITEM"))
                .andExpect(jsonPath("$[1].type").value("LEVEL_CARD"));
    }
}
