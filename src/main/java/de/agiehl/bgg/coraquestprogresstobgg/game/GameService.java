package de.agiehl.bgg.coraquestprogresstobgg.game;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {

    private static final String CODE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 4;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final GameRepository gameRepository;

    public Game createGame() {
        String code = generateUniqueCode();
        log.info("Creating new game with code {}", code);
        Game game = new Game();
        game.setCode(code);
        game.setDifficulty("NORMAL");
        game.setGameLength("SHORT");
        return gameRepository.save(game);
    }

    public Optional<Game> findByCode(String code) {
        return gameRepository.findByCode(code.toUpperCase());
    }

    private String generateUniqueCode() {
        String code;
        do {
            code = generateCode();
        } while (gameRepository.existsByCode(code));
        return code;
    }

    private String generateCode() {
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(CODE_CHARACTERS.charAt(RANDOM.nextInt(CODE_CHARACTERS.length())));
        }
        return sb.toString();
    }
}
