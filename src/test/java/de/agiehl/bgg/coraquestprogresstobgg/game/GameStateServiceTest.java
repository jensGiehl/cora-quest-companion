package de.agiehl.bgg.coraquestprogresstobgg.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameStateServiceTest {

    @Mock
    private GameRepository gameRepository;
    @Mock
    private GameCurseRepository gameCurseRepository;
    @Mock
    private PlayerCharacterRepository playerCharacterRepository;
    @Mock
    private PlayerCardRepository playerCardRepository;

    @InjectMocks
    private GameStateService gameStateService;

    private final Game game = new Game(1L, "ABCD", null, "NORMAL", false, 0);

    @Test
    void getStateReturnsFullState() {
        when(gameRepository.findByCode("ABCD")).thenReturn(Optional.of(game));
        when(gameCurseRepository.findByGameId(1L)).thenReturn(List.of(new GameCurse(1L, 1L, "Poison")));
        when(playerCharacterRepository.findByGameId(1L)).thenReturn(List.of());

        GameStateResponse state = gameStateService.getState("ABCD");

        assertThat(state.curses()).containsExactly("Poison");
        assertThat(state.difficulty()).isEqualTo("NORMAL");
        assertThat(state.characters()).isEmpty();
    }

    @Test
    void updateStateSavesGameFieldsAndReplacesAllCurses() {
        when(gameRepository.findByCode("ABCD")).thenReturn(Optional.of(game));
        when(gameRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var update = new GameStateUpdate("Quest 1", "HARD", true, 5, List.of("Poison", "Blind"));
        gameStateService.updateState("ABCD", update);

        verify(gameRepository).save(argThat(g ->
                "Quest 1".equals(g.getQuestName()) && "HARD".equals(g.getDifficulty())
                && g.isSecondWind() && g.getGold() == 5));
        verify(gameCurseRepository).deleteByGameId(1L);
        verify(gameCurseRepository, times(2)).save(any(GameCurse.class));
    }

    @Test
    void addCharacterPersistsAndReturnsNewCharacter() {
        when(gameRepository.findByCode("ABCD")).thenReturn(Optional.of(game));
        PlayerCharacter saved = new PlayerCharacter(42L, 1L, null, null, 1);
        when(playerCharacterRepository.save(any())).thenReturn(saved);
        when(playerCardRepository.findByCharacterId(42L)).thenReturn(List.of());

        PlayerCharacterResponse response = gameStateService.addCharacter("ABCD");

        assertThat(response.id()).isEqualTo(42L);
        assertThat(response.health()).isEqualTo(1);
        assertThat(response.cards()).isEmpty();
    }

    @Test
    void deleteCharacterRemovesCardsFirst() {
        gameStateService.deleteCharacter(7L);

        verify(playerCardRepository).deleteByCharacterId(7L);
        verify(playerCharacterRepository).deleteById(7L);
    }

    @Test
    void addCardPersistsAndReturnsCardResponse() {
        PlayerCard saved = new PlayerCard(10L, 7L, "Sword", "BASE", "ITEM");
        when(playerCardRepository.save(any())).thenReturn(saved);

        PlayerCardResponse response = gameStateService.addCard(7L, new CardAdd("Sword", "BASE", "ITEM"));

        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.cardName()).isEqualTo("Sword");
    }
}
