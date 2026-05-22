package de.agiehl.bgg.coraquestprogresstobgg.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("game_curse")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameCurse {

    @Id
    private Long id;
    private Long gameId;
    private String curseDescription;
}
