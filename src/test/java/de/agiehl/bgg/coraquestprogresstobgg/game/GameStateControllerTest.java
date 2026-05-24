package de.agiehl.bgg.coraquestprogresstobgg.game;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GameStateController.class)
class GameStateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private GameStateService gameStateService;

    private final Game knownGame = new Game(1L, "ABCD", null, "NORMAL", "SHORT", null, 0);

    @Test
    void getStateReturns404ForUnknownCode() throws Exception {
        when(gameService.findByCode("XXXX")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/game/XXXX/state"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getStateReturnsGameState() throws Exception {
        when(gameService.findByCode("ABCD")).thenReturn(Optional.of(knownGame));
        when(gameStateService.getState("ABCD")).thenReturn(
                new GameStateResponse("Quest 1", "NORMAL", "SHORT", null, 3, List.of("Poison"), List.of()));

        mockMvc.perform(get("/api/game/ABCD/state"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questName").value("Quest 1"))
                .andExpect(jsonPath("$.difficulty").value("NORMAL"))
                .andExpect(jsonPath("$.gold").value(3))
                .andExpect(jsonPath("$.curses[0]").value("Poison"));
    }

    @Test
    void updateStateReturns204() throws Exception {
        when(gameService.findByCode("ABCD")).thenReturn(Optional.of(knownGame));

        mockMvc.perform(patch("/api/game/ABCD/state")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"questName\":\"Quest 1\",\"difficulty\":\"HARD\",\"gameLength\":\"LONG\",\"secondWindCharacterId\":1,\"gold\":5,\"curses\":[]}"))
                .andExpect(status().isNoContent());

        verify(gameStateService).updateState(eq("ABCD"), any(GameStateUpdate.class));
    }

    @Test
    void addCharacterReturnsNewCharacter() throws Exception {
        when(gameService.findByCode("ABCD")).thenReturn(Optional.of(knownGame));
        when(gameStateService.addCharacter("ABCD")).thenReturn(
                new PlayerCharacterResponse(42L, null, null, 1, List.of()));

        mockMvc.perform(post("/api/game/ABCD/characters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(42))
                .andExpect(jsonPath("$.health").value(1));
    }

    @Test
    void deleteCharacterReturns204() throws Exception {
        mockMvc.perform(delete("/api/game/ABCD/characters/7"))
                .andExpect(status().isNoContent());

        verify(gameStateService).deleteCharacter(7L);
    }

    @Test
    void addCardReturnsCreatedCard() throws Exception {
        when(gameStateService.addCard(eq(7L), any(CardAdd.class))).thenReturn(
                new PlayerCardResponse(10L, "Sword", "BASE", "ITEM"));

        mockMvc.perform(post("/api/game/ABCD/characters/7/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cardName\":\"Sword\",\"cardSource\":\"BASE\",\"cardType\":\"ITEM\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.cardName").value("Sword"));
    }

    @Test
    void deleteCardReturns204() throws Exception {
        mockMvc.perform(delete("/api/game/ABCD/characters/7/cards/10"))
                .andExpect(status().isNoContent());

        verify(gameStateService).deleteCard(10L);
    }
}