package de.agiehl.bgg.coraquestprogresstobgg.game;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class GameController {

    private static final String COOKIE_NAME = "gameCode";
    private static final int COOKIE_MAX_AGE = 30 * 24 * 60 * 60;

    private final GameService gameService;

    @GetMapping("/")
    public String index(HttpServletRequest request) {
        return readGameCodeFromCookie(request)
                .flatMap(gameService::findByCode)
                .map(game -> "redirect:/game/" + game.getCode())
                .orElse("index");
    }

    @PostMapping("/create")
    public String create(HttpServletResponse response) {
        Game game = gameService.createGame();
        addGameCodeCookie(response, game.getCode());
        return "redirect:/game/" + game.getCode();
    }

    @PostMapping("/join")
    public String join(@RequestParam String code, HttpServletResponse response, Model model) {
        return gameService.findByCode(code)
                .map(game -> {
                    addGameCodeCookie(response, game.getCode());
                    return "redirect:/game/" + game.getCode();
                })
                .orElseGet(() -> {
                    model.addAttribute("error", "Game with code \"" + code.toUpperCase() + "\" not found.");
                    return "index";
                });
    }

    @GetMapping("/game/{code}")
    public String game(@PathVariable String code, HttpServletResponse response, Model model) {
        if (gameService.findByCode(code).isEmpty()) {
            clearGameCodeCookie(response);
            return "redirect:/";
        }
        model.addAttribute("code", code.toUpperCase());
        return "game";
    }

    @GetMapping("/leave")
    public String leave(HttpServletResponse response) {
        clearGameCodeCookie(response);
        return "redirect:/";
    }

    private Optional<String> readGameCodeFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        return Arrays.stream(cookies)
                .filter(c -> COOKIE_NAME.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }

    private void addGameCodeCookie(HttpServletResponse response, String code) {
        Cookie cookie = new Cookie(COOKIE_NAME, code);
        cookie.setPath("/");
        cookie.setMaxAge(COOKIE_MAX_AGE);
        response.addCookie(cookie);
    }

    private void clearGameCodeCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(COOKIE_NAME, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
