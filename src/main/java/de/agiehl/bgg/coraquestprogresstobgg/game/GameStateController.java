package de.agiehl.bgg.coraquestprogresstobgg.game;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GameStateController {

    private final GameService gameService;
    private final GameStateService gameStateService;

    @GetMapping("/{code}/state")
    public ResponseEntity<GameStateResponse> getState(@PathVariable String code) {
        if (gameService.findByCode(code).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(gameStateService.getState(code));
    }

    @PatchMapping("/{code}/state")
    public ResponseEntity<Void> updateState(@PathVariable String code, @RequestBody GameStateUpdate update) {
        if (gameService.findByCode(code).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        gameStateService.updateState(code, update);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{code}/characters")
    public ResponseEntity<PlayerCharacterResponse> addCharacter(@PathVariable String code) {
        if (gameService.findByCode(code).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(gameStateService.addCharacter(code));
    }

    @PatchMapping("/{code}/characters/{characterId}")
    public ResponseEntity<Void> updateCharacter(@PathVariable String code,
                                                @PathVariable Long characterId,
                                                @RequestBody CharacterUpdate update) {
        gameStateService.updateCharacter(characterId, update);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{code}/characters/{characterId}")
    public ResponseEntity<Void> deleteCharacter(@PathVariable String code, @PathVariable Long characterId) {
        gameStateService.deleteCharacter(characterId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{code}/characters/{characterId}/cards")
    public ResponseEntity<PlayerCardResponse> addCard(@PathVariable String code,
                                                      @PathVariable Long characterId,
                                                      @RequestBody CardAdd cardAdd) {
        return ResponseEntity.ok(gameStateService.addCard(characterId, cardAdd));
    }

    @DeleteMapping("/{code}/characters/{characterId}/cards/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable String code,
                                           @PathVariable Long characterId,
                                           @PathVariable Long cardId) {
        gameStateService.deleteCard(cardId);
        return ResponseEntity.noContent().build();
    }
}
