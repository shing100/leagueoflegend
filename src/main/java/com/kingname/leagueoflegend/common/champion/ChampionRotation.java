package com.kingname.leagueoflegend.common.champion;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity @ToString
@Setter @Getter @EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor @AllArgsConstructor
public class ChampionRotation {

    @Id @GeneratedValue
    private Long id;

    private String[] freeChampionIds;

    private String[] freeChampionIdsForNewPlayers;

    private int maxNewPlayerLevel;

    private LocalDateTime createDt;
}
