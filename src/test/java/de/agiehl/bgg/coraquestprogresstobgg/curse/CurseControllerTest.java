package de.agiehl.bgg.coraquestprogresstobgg.curse;

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

@WebMvcTest(CurseController.class)
class CurseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CurseService curseService;

    @Test
    void returnsCursesAsJson() throws Exception {
        when(curseService.findAll()).thenReturn(List.of(
                new Curse("You lose 1 health"),
                new Curse("Skip your next turn")
        ));

        mockMvc.perform(get("/api/curses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("You lose 1 health"))
                .andExpect(jsonPath("$[1].description").value("Skip your next turn"));
    }
}
