package de.agiehl.bgg.coraquestprogresstobgg.game;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameStateService {

    private final GameRepository gameRepository;
    private final GameCurseRepository gameCurseRepository;
    private final PlayerCharacterRepository playerCharacterRepository;
    private final PlayerCardRepository playerCardRepository;

    public GameStateResponse getState(String code) {
        Game game = findGame(code);
        List<String> curses = gameCurseRepository.findByGameId(game.getId())
                .stream().map(GameCurse::getCurseDescription).toList();
        List<PlayerCharacterResponse> characters = playerCharacterRepository.findByGameId(game.getId())
                .stream().map(this::toCharacterResponse).toList();
        return new GameStateResponse(game.getQuestName(), game.getDifficulty(), game.isSecondWind(), game.getGold(), curses, characters);
    }

    public void updateState(String code, GameStateUpdate update) {
        Game game = findGame(code);
        game.setQuestName(update.questName());
        game.setDifficulty(update.difficulty() != null ? update.difficulty() : "NORMAL");
        game.setSecondWind(update.secondWind());
        game.setGold(update.gold());
        gameRepository.save(game);

        gameCurseRepository.deleteByGameId(game.getId());
        if (update.curses() != null) {
            update.curses().forEach(desc -> gameCurseRepository.save(new GameCurse(null, game.getId(), desc)));
        }
        log.debug("Updated state for game {}", code);
    }

    public PlayerCharacterResponse addCharacter(String code) {
        Game game = findGame(code);
        PlayerCharacter pc = new PlayerCharacter();
        pc.setGameId(game.getId());
        pc.setHealth(1);
        PlayerCharacter saved = playerCharacterRepository.save(pc);
        return toCharacterResponse(saved);
    }

    public void updateCharacter(Long characterId, CharacterUpdate update) {
        PlayerCharacter pc = playerCharacterRepository.findById(characterId).orElseThrow();
        pc.setPlayerName(update.playerName());
        pc.setCharacterName(update.characterName());
        pc.setHealth(update.health());
        playerCharacterRepository.save(pc);
    }

    public void deleteCharacter(Long characterId) {
        playerCardRepository.deleteByCharacterId(characterId);
        playerCharacterRepository.deleteById(characterId);
    }

    public PlayerCardResponse addCard(Long characterId, CardAdd cardAdd) {
        PlayerCard card = new PlayerCard(null, characterId, cardAdd.cardName(), cardAdd.cardSource(), cardAdd.cardType());
        PlayerCard saved = playerCardRepository.save(card);
        return toCardResponse(saved);
    }

    public void deleteCard(Long cardId) {
        playerCardRepository.deleteById(cardId);
    }

    private Game findGame(String code) {
        return gameRepository.findByCode(code.toUpperCase()).orElseThrow();
    }

    private PlayerCharacterResponse toCharacterResponse(PlayerCharacter pc) {
        List<PlayerCardResponse> cards = playerCardRepository.findByCharacterId(pc.getId())
                .stream().map(this::toCardResponse).toList();
        return new PlayerCharacterResponse(pc.getId(), pc.getPlayerName(), pc.getCharacterName(), pc.getHealth(), cards);
    }

    private PlayerCardResponse toCardResponse(PlayerCard card) {
        return new PlayerCardResponse(card.getId(), card.getCardName(), card.getCardSource(), card.getCardType());
    }
}
