package com.kingname.leagueoflegend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("url")
public class AppProperties {

    private String apikey;

    private String champion;
    private String leaderboards;
    private String activeGame;
    private String championMastery;
    private String summoner;
    private String summonerId;
    private String league;
    private String matchlists;
    private String matchInfo;
}
