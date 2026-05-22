package de.agiehl.bgg.coraquestprogresstobgg.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("player_character")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerCharacter {

    @Id
    private Long id;
    private Long gameId;
    private String playerName;
    private String characterName;
    private int health;
}
