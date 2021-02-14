package com.kingname.leagueoflegend.user.summoner;

import com.kingname.leagueoflegend.user.champion.ChampionMastery;
import com.kingname.leagueoflegend.user.league.League;
import com.kingname.leagueoflegend.user.match.Match;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@ToString @Setter @Getter
@EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor @AllArgsConstructor
public class Summoner {

    @Id
    private String id;

    @Column(unique = true, nullable = false)
    private String name;

    private String accountId;
    private String puuid;
    private String profileIconId;
    private Date revisionDate;
    private String summonerLevel;

    private LocalDateTime updateDt;

    @ManyToMany
    private List<League> leagueList = new ArrayList<>();

    @ManyToMany
    private List<ChampionMastery> championMasteryList = new ArrayList<>();

    @ManyToMany
    private List<Match> matches = new ArrayList<>();

    public String getProfileImage() {
        return "http://ddragon.leagueoflegends.com/cdn/11.3.1/img/profileicon/" + this.profileIconId + ".png";
    }
}
