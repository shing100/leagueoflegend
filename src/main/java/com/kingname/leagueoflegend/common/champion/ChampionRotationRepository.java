package com.kingname.leagueoflegend.common.champion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChampionRotationRepository extends JpaRepository<ChampionRotation, Long> {

}
