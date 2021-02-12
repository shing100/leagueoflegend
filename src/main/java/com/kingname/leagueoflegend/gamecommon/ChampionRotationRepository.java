package com.kingname.leagueoflegend.gamecommon;

import com.kingname.leagueoflegend.gamecommon.vo.ChampionRotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChampionRotationRepository extends JpaRepository<ChampionRotation, Long> {

}
