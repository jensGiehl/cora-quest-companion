package de.agiehl.bgg.coraquestprogresstobgg.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;

    @Test
    void createGameReturnsFourCharAlphanumericCode() {
        when(gameRepository.existsByCode(anyString())).thenReturn(false);
        when(gameRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Game game = gameService.createGame();

        assertThat(game.getCode()).hasSize(4);
        assertThat(game.getCode()).matches("[A-Z0-9]{4}");
    }

    @Test
    void createGameRetriesUntilUniqueCodeIsFound() {
        when(gameRepository.existsByCode(anyString())).thenReturn(true, true, false);
        when(gameRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        gameService.createGame();

        verify(gameRepository, times(3)).existsByCode(anyString());
    }

    @Test
    void findByCodeNormalizesInputToUppercase() {
        Game game = new Game(1L, "ABCD", null, "NORMAL", false, 0);
        when(gameRepository.findByCode("ABCD")).thenReturn(Optional.of(game));

        assertThat(gameService.findByCode("abcd")).contains(game);
    }

    @Test
    void findByCodeReturnsEmptyForUnknownCode() {
        when(gameRepository.findByCode(anyString())).thenReturn(Optional.empty());

        assertThat(gameService.findByCode("XXXX")).isEmpty();
    }
}
