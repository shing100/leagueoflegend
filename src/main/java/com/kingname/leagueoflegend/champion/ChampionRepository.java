package com.kingname.leagueoflegend.champion;

import com.kingname.leagueoflegend.champion.vo.ChampionRotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChampionRepository extends JpaRepository<ChampionRotation, Long> {

}
