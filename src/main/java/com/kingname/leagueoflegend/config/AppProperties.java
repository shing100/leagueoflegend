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
    private String summoner;
    private String league;

}
