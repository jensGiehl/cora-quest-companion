package de.agiehl.bgg.coraquestprogresstobgg.game;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GameController.class)
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GameService gameService;

    @Test
    void indexRendersIndexPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void indexRedirectsToGameWhenCookieIsValid() throws Exception {
        when(gameService.findByCode("ABCD")).thenReturn(Optional.of(new Game(1L, "ABCD", null, "NORMAL", null, null, 0)));

        mockMvc.perform(get("/").cookie(new Cookie("gameCode", "ABCD")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/game/ABCD"));
    }

    @Test
    void indexRendersIndexPageWhenCookieCodeIsUnknown() throws Exception {
        when(gameService.findByCode("XXXX")).thenReturn(Optional.empty());

        mockMvc.perform(get("/").cookie(new Cookie("gameCode", "XXXX")))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void createRedirectsToNewGame() throws Exception {
        when(gameService.createGame()).thenReturn(new Game(1L, "ABCD", null, "NORMAL", null, null, 0));

        mockMvc.perform(post("/create"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/game/ABCD"));
    }

    @Test
    void joinRedirectsToGameOnValidCode() throws Exception {
        when(gameService.findByCode("ABCD")).thenReturn(Optional.of(new Game(1L, "ABCD", null, "NORMAL", null, null, 0)));

        mockMvc.perform(post("/join").param("code", "ABCD"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/game/ABCD"));
    }

    @Test
    void joinShowsErrorForInvalidCode() throws Exception {
        when(gameService.findByCode("XXXX")).thenReturn(Optional.empty());

        mockMvc.perform(post("/join").param("code", "XXXX"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    void gamePageRedirectsToStartForUnknownCode() throws Exception {
        when(gameService.findByCode("XXXX")).thenReturn(Optional.empty());

        mockMvc.perform(get("/game/XXXX"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void gamePageClearsCookieForUnknownCode() throws Exception {
        when(gameService.findByCode("XXXX")).thenReturn(Optional.empty());

        mockMvc.perform(get("/game/XXXX"))
                .andExpect(cookie().maxAge("gameCode", 0));
    }

    @Test
    void leaveRedirectsToStart() throws Exception {
        mockMvc.perform(get("/leave"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void leaveClearsCookie() throws Exception {
        mockMvc.perform(get("/leave"))
                .andExpect(cookie().maxAge("gameCode", 0));
    }
}