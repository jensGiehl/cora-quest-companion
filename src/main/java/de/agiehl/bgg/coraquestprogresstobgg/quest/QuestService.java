package de.agiehl.bgg.coraquestprogresstobgg.quest;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class QuestService {

    private List<Quest> quests;

    @PostConstruct
    void loadQuests() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ClassPathResource resource = new ClassPathResource("quests.json");
        var listType = mapper.getTypeFactory().constructCollectionType(List.class, Quest.class);
        quests = mapper.readValue(resource.getInputStream(), listType);
        log.info("Loaded {} quests", quests.size());
    }

    public List<Quest> findAll() {
        return List.copyOf(quests);
    }
}
