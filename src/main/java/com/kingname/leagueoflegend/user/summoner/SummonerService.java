package com.kingname.leagueoflegend.user.summoner;

import com.kingname.leagueoflegend.config.AppProperties;
import com.kingname.leagueoflegend.user.champion.ChampionMastery;
import com.kingname.leagueoflegend.user.champion.ChampionMasteryRepository;
import com.kingname.leagueoflegend.user.league.League;
import com.kingname.leagueoflegend.user.league.LeagueRepository;
import com.kingname.leagueoflegend.user.spectator.ActiveGame;
import com.kingname.leagueoflegend.user.spectator.Participants;
import com.kingname.leagueoflegend.util.Global;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SummonerService {

    private final RestTemplate restTemplate;
    private final AppProperties appProperties;
    private final SummonerRepository summonerRepository;
    private final LeagueRepository leagueRepository;
    private final ChampionMasteryRepository championMasteryRepository;
    private final ModelMapper modelMapper;

    public Summoner getSummoner(String name) {
        Summoner byName = summonerRepository.findByName(name);
        if (byName != null) {
            return byName;
        }
        return updateSummoner(name);
    }

    public Summoner getSummonerLeagueInfo(String name) {
        Summoner summoner = getSummoner(name);
        if (summoner.getLeagueList().size() > 0) {
            return summoner;
        }
        return updateSummonerLeague(summoner);
    }

    public Summoner getChampionMasteryByUsername(String name) {
        Summoner summoner = getSummoner(name);
        if (summoner.getChampionMasteryList().size() > 0) {
            return summoner;
        }
        return updateChampionMastery(summoner);
    }

    public ActiveGame getActiveGame(String name) {
        ActiveGame activeGame = new ActiveGame();
        Summoner summoner = getSummoner(name);
        String requestURL = appProperties.getActiveGame() + summoner.getId();
        HttpEntity<Object> httpEntity = setHttpContext();
        try {
            activeGame = restTemplate.exchange(requestURL, HttpMethod.GET, httpEntity, ActiveGame.class).getBody();
            List<Participants> participants = activeGame.getParticipants();
            participants.forEach(player -> {
                String summonerName = player.getSummonerName();
                Summoner summonerLeagueInfo = getSummonerLeagueInfo(summonerName);
                List<League> leagueList = summonerLeagueInfo.getLeagueList();
                if (leagueList.size() > 0) {
                    for (League league : leagueList) {
                        player.setTier(league.getTier());
                        player.setRank(league.getRank());
                        player.setWins(league.getWins());
                        player.setLosses(league.getLosses());
                        player.setLeaguePoints(league.getLeaguePoints());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            activeGame.setGameId("notFound");
        } finally {
            return activeGame;
        }
    }

    private Summoner updateChampionMastery(Summoner summoner) {
        String requestURL = appProperties.getChampionMastery() + summoner.getId();
        HttpEntity<Object> httpEntity = setHttpContext();
        List<HashMap<String, Object>> championMasterylist = restTemplate.exchange(requestURL, HttpMethod.GET, httpEntity, List.class).getBody();
        for (HashMap<String, Object> champion : championMasterylist) {
            ChampionMastery championMastery = modelMapper.map(champion, ChampionMastery.class);
            championMastery.setId(championMastery.getSummonerId() + championMastery.getChampionId());
            championMasteryRepository.save(championMastery);
            summoner.getChampionMasteryList().add(championMastery);
        }
        return summonerRepository.save(summoner);
    }

    private Summoner updateSummoner(String name) {
        String requestURL = appProperties.getSummoner() + name;
        HttpEntity httpEntity = setHttpContext();
        log.info(requestURL);
        Summoner summoner = restTemplate.exchange(requestURL, HttpMethod.GET, httpEntity, Summoner.class).getBody();
        Objects.requireNonNull(summoner).setUpdateDt(LocalDateTime.now());
        return summonerRepository.save(summoner);
    }

    private Summoner updateSummonerLeague(Summoner summoner) {
        String requestURL = appProperties.getLeague() + summoner.getId();
        HttpEntity<Object> httpEntity = setHttpContext();
        List<HashMap<String, Object>> leagues = restTemplate.exchange(requestURL, HttpMethod.GET, httpEntity, List.class).getBody();
        for (HashMap<String, Object> league : leagues) {
            League summonerLeague = modelMapper.map(league, League.class);
            summonerLeague.setId(summonerLeague.getLeagueId() + summonerLeague.getSummonerId());
            League save = leagueRepository.save(summonerLeague);
            summoner.getLeagueList().add(save);
        }
        return summonerRepository.save(summoner);
    }

    private HttpEntity<Object> setHttpContext() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(Global.X_Riot_Token, appProperties.getApikey());
        return new HttpEntity<>(httpHeaders);
    }
}
