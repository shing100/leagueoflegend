package com.kingname.leagueoflegend.user.champion;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@ToString @Setter @Getter
@EqualsAndHashCode(of = "id") @Builder
@NoArgsConstructor @AllArgsConstructor
public class ChampionMastery {

    @Id
    private String id;

    private String championId;
    private String championLevel;
    private String championPoints;

    private Date lastPlayTime;
    private String championPointsSinceLastLevel;
    private String championPointsUntilNextLevel;

    private Boolean chestGranted;
    private int tokensEarned;
    private String summonerId;
}
