package de.agiehl.bgg.coraquestprogresstobgg.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("player_card")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerCard {

    @Id
    private Long id;
    private Long characterId;
    private String cardName;
    private String cardSource;
    private String cardType;
}
