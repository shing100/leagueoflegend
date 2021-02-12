package com.kingname.leagueoflegend.summoner;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeagueRepository extends JpaRepository<League, Long> {
    List<League> findBySummonerName(String name);
}
