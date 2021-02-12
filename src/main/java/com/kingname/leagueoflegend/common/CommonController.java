package com.kingname.leagueoflegend.common;

import com.kingname.leagueoflegend.common.champion.ChampionRotation;
import com.kingname.leagueoflegend.common.player.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommonController {

    private final CommonService commonService;

    @GetMapping("/championRotation")
    public ResponseEntity<ChampionRotation> viewChampion() {
        ChampionRotation championList = commonService.getChampionList();
        return new ResponseEntity<>(championList, HttpStatus.OK);
    }

    @GetMapping("/rankList")
    public ResponseEntity<List<Player>> viewLolRank() {
        List<Player> list = commonService.getLolAsiaRankList();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
