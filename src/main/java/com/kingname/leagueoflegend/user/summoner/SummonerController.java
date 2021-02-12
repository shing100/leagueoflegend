package com.kingname.leagueoflegend.user.summoner;

import com.kingname.leagueoflegend.user.spectator.ActiveGame;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SummonerController {

    private final SummonerService summonerService;

    @GetMapping("/summoner/{username}")
    public ResponseEntity<Summoner> viewSummoner(@PathVariable String username) {
        Summoner summoner = summonerService.getSummoner(username);
        return new ResponseEntity<>(summoner, HttpStatus.OK);
    }

    @GetMapping("/summoner/rank/{username}")
    public ResponseEntity<Summoner> viewRankSummoner(@PathVariable String username) {
        Summoner summoner = summonerService.getSummonerLeagueInfo(username);
        return new ResponseEntity<>(summoner, HttpStatus.OK);
    }

    @GetMapping("/summoner/mastery/{username}")
    public ResponseEntity<Summoner> viewMasterySummoner(@PathVariable String username) {
        Summoner summoner = summonerService.getChampionMasteryByUsername(username);
        return new ResponseEntity<>(summoner, HttpStatus.OK);
    }

    @GetMapping("/summoner/active/{username}")
    public ResponseEntity<ActiveGame> viewActiveGameSummoner(@PathVariable String username) {
        ActiveGame activeGame = summonerService.getActiveGame(username);
        return new ResponseEntity<>(activeGame, HttpStatus.OK);
    }
}
