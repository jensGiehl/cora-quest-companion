package de.agiehl.bgg.coraquestprogresstobgg.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("game")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    @Id
    private Long id;
    private String code;
    private String questName;
    private String difficulty;
    private String gameLength;
    private boolean secondWind;
    private int gold;
}
