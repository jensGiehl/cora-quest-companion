package de.agiehl.bgg.coraquestprogresstobgg.curse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CurseServiceTest {

    private CurseService curseService;

    @BeforeEach
    void setUp() throws Exception {
        curseService = new CurseService();
        curseService.loadCurses();
    }

    @Test
    void loadsCursesFromJson() {
        assertThat(curseService.findAll()).isNotEmpty();
    }

    @Test
    void allCursesHaveDescription() {
        curseService.findAll().forEach(curse ->
                assertThat(curse.description()).isNotBlank()
        );
    }
}
