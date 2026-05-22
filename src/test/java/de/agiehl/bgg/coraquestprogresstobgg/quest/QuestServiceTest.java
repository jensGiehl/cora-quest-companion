package de.agiehl.bgg.coraquestprogresstobgg.quest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class QuestServiceTest {

    private QuestService questService;

    @BeforeEach
    void setUp() throws Exception {
        questService = new QuestService();
        questService.loadQuests();
    }

    @Test
    void loadsQuestsFromJson() {
        assertThat(questService.findAll()).isNotEmpty();
    }

    @Test
    void allQuestsHaveNameAndSource() {
        questService.findAll().forEach(quest -> {
            assertThat(quest.name()).isNotBlank();
            assertThat(quest.source()).isNotNull();
        });
    }
}
