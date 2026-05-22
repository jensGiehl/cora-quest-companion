package de.agiehl.bgg.coraquestprogresstobgg.card;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CardServiceTest {

    private CardService cardService;

    @BeforeEach
    void setUp() throws Exception {
        cardService = new CardService();
        cardService.loadCards();
    }

    @Test
    void loadsCardsFromJson() {
        assertThat(cardService.findAll()).isNotEmpty();
    }

    @Test
    void allCardsHaveNameSourceAndType() {
        cardService.findAll().forEach(card -> {
            assertThat(card.name()).isNotBlank();
            assertThat(card.source()).isNotNull();
            assertThat(card.type()).isNotNull();
        });
    }
}
