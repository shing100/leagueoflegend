package com.kingname.leagueoflegend.user.summoner;

import com.kingname.leagueoflegend.common.Global;
import com.kingname.leagueoflegend.config.AppProperties;
import com.kingname.leagueoflegend.user.champion.ChampionMastery;
import com.kingname.leagueoflegend.user.champion.ChampionMasteryRepository;
import com.kingname.leagueoflegend.user.league.League;
import com.kingname.leagueoflegend.user.league.LeagueRepository;
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
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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
        String summonerName = name.replaceAll(" ", "%20");
        String requestURL = appProperties.getSummoner() + summonerName;
        HttpEntity httpEntity = setHttpContext();
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
        return summoner;
    }

    private HttpEntity<Object> setHttpContext() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(Global.X_Riot_Token, appProperties.getApikey());
        return new HttpEntity<>(httpHeaders);
    }
}
