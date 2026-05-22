package de.agiehl.bgg.coraquestprogresstobgg.character;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class CharacterService {

    private List<GameCharacter> characters;

    @PostConstruct
    void loadCharacters() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ClassPathResource resource = new ClassPathResource("characters.json");
        var listType = mapper.getTypeFactory().constructCollectionType(List.class, GameCharacter.class);
        characters = mapper.readValue(resource.getInputStream(), listType);
        log.info("Loaded {} characters", characters.size());
    }

    public List<GameCharacter> findAll() {
        return List.copyOf(characters);
    }
}
