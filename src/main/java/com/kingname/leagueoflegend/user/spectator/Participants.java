package com.kingname.leagueoflegend.user.spectator;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Participants {

    private String teamId;
    private String spell1Id;
    private String spell2Id;
    private String championId;
    private String profileIconId;
    private String summonerName;
    private String tier;
    private String rank;
    private int leaguePoints;
    private int wins;
    private int losses;
    private String bot;
    private String summonerId;
    private List gameCustomizationObjects;
    private Map<String, Object> perks;
}
