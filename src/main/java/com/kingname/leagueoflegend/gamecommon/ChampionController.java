package com.kingname.leagueoflegend.gamecommon;

import com.kingname.leagueoflegend.gamecommon.vo.ChampionRotation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChampionController {

    private final ChampionService championService;

    @GetMapping("/championRotation")
    public ResponseEntity<ChampionRotation> viewChampion() {
        ChampionRotation championList = championService.getChampionList();
        return new ResponseEntity<>(championList, HttpStatus.OK);
    }
}
