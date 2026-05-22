package de.agiehl.bgg.coraquestprogresstobgg.quest;

import de.agiehl.bgg.coraquestprogresstobgg.common.GameSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuestController.class)
class QuestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private QuestService questService;

    @Test
    void returnsQuestsAsJson() throws Exception {
        when(questService.findAll()).thenReturn(List.of(
                new Quest("Test Quest", GameSource.BASE),
                new Quest("Expansion Quest", GameSource.KEEP_QUESTING)
        ));

        mockMvc.perform(get("/api/quests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Quest"))
                .andExpect(jsonPath("$[0].source").value("BASE"))
                .andExpect(jsonPath("$[1].source").value("KEEP_QUESTING"));
    }
}
