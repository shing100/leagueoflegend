package com.kingname.leagueoflegend.user.summoner;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SummonerRepository extends JpaRepository<Summoner, String> {

    Summoner findByName(String name);
}
