package com.kingname.leagueoflegend.summoner;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@ToString @Setter @Getter
@EqualsAndHashCode(of = "id") @Builder
@NoArgsConstructor @AllArgsConstructor
public class League {

    @Id
    private String id;
    private String leagueId;
    private String queueType;
    private String tier;
    private String rank;
    private String summonerId;
    private String summonerName;

    private int leaguePoints;
    private int wins;
    private int losses;

    private Boolean veteran;
    private Boolean inactive;
    private Boolean freshBlood;
    private Boolean hotStreak;
}
