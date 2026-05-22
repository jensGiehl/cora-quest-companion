package de.agiehl.bgg.coraquestprogresstobgg.curse;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/curses")
@RequiredArgsConstructor
public class CurseController {

    private final CurseService curseService;

    @GetMapping
    public List<Curse> findAll() {
        return curseService.findAll();
    }
}
