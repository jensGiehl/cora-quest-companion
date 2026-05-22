package de.agiehl.bgg.coraquestprogresstobgg.curse;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class CurseService {

    private List<Curse> curses;

    @PostConstruct
    void loadCurses() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ClassPathResource resource = new ClassPathResource("curses.json");
        var listType = mapper.getTypeFactory().constructCollectionType(List.class, Curse.class);
        curses = mapper.readValue(resource.getInputStream(), listType);
        log.info("Loaded {} curses", curses.size());
    }

    public List<Curse> findAll() {
        return List.copyOf(curses);
    }
}
