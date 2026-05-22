package de.agiehl.bgg.coraquestprogresstobgg.card;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class CardService {

    private List<Card> cards;

    @PostConstruct
    void loadCards() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ClassPathResource resource = new ClassPathResource("cards.json");
        var listType = mapper.getTypeFactory().constructCollectionType(List.class, Card.class);
        cards = mapper.readValue(resource.getInputStream(), listType);
        log.info("Loaded {} cards", cards.size());
    }

    public List<Card> findAll() {
        return List.copyOf(cards);
    }
}
