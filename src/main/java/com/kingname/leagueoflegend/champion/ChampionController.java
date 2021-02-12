package com.kingname.leagueoflegend.champion;

import com.kingname.leagueoflegend.champion.vo.ChampionRotation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChampionController {

    private final ChampionService championService;

    @GetMapping("/champion")
    public ResponseEntity<ChampionRotation> viewChampion() {
        ChampionRotation championList = championService.getChampionList();
        return new ResponseEntity<>(championList, HttpStatus.OK);
    }
}
