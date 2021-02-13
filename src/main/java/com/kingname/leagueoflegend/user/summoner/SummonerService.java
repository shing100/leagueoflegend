package com.kingname.leagueoflegend.user.summoner;

import com.kingname.leagueoflegend.config.AppProperties;
import com.kingname.leagueoflegend.user.champion.ChampionMastery;
import com.kingname.leagueoflegend.user.champion.ChampionMasteryRepository;
import com.kingname.leagueoflegend.user.league.League;
import com.kingname.leagueoflegend.user.league.LeagueRepository;
import com.kingname.leagueoflegend.user.match.Match;
import com.kingname.leagueoflegend.user.match.MatchRepository;
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
    private final MatchRepository matchRepository;
    private final ModelMapper modelMapper;

    public Summoner getSummoner(String name) {
        Summoner byName = summonerRepository.findByName(name);
        if (byName != null) {
            return byName;
        }
        return updateBaseInfoSummoner(name);
    }

    public Summoner getSummonerLeagueInfo(String name) {
        Summoner summoner = getSummoner(name);
        if (summoner.getLeagueList().size() > 0) {
            return summoner;
        }
        return updateSummonerLeague(summoner);
    }

    public Summoner getChampionMastery(String name) {
        Summoner summoner = getSummoner(name);
        if (summoner.getChampionMasteryList().size() > 0) {
            return summoner;
        }
        return updateChampionMastery(summoner);
    }

    public Summoner getMatchList(String name, int page) {
        Summoner summoner = getSummoner(name);
        if (summoner.getMatches().size() > 0) {
            return summoner;
        }
        return updateMatchList(summoner, page);
    }

    public Summoner updateSummoner(String name) {
        Summoner summoner = updateBaseInfoSummoner(name);
        summoner = updateChampionMastery(summoner);
        summoner = updateMatchList(summoner, 1);
        summoner = updateSummonerLeague(summoner);
        return summoner;
    }

    private Summoner updateMatchList(Summoner summoner, int page) {
        int pageSize = 20;
        int beginIndex = (page - 1) * pageSize;
        int endIndex = beginIndex + pageSize;
        String requestURL = appProperties.getMatchlists() + summoner.getAccountId() + "?endIndex=" + endIndex +"&beginIndex=" + beginIndex;
        log.info(requestURL);
        HttpEntity<Object> httpEntity = setHttpContext();
        Map<String, List<Map<String, Object>>> matches = restTemplate.exchange(requestURL, HttpMethod.GET, httpEntity, Map.class).getBody();
        List<Map<String, Object>> matchList = matches.get("matches");
        for (Map<String, Object> match : matchList) {
            Match newMatch = modelMapper.map(match, Match.class);
            Match save = matchRepository.save(newMatch);
            summoner.getMatches().add(save);
        }
        return summonerRepository.save(summoner);
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
                    League league = leagueList.get(leagueList.size() - 1);
                    modelMapper.map(league, player);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            activeGame.setGameId("게임을 찾을 수 없습니다.");
            activeGame.setGameMode("n");
        } finally {
            return activeGame;
        }
    }

    private Summoner updateChampionMastery(Summoner summoner) {
        String requestURL = appProperties.getChampionMastery() + summoner.getId();
        HttpEntity<Object> httpEntity = setHttpContext();
        List<Map<String, Object>> championMasterylist = restTemplate.exchange(requestURL, HttpMethod.GET, httpEntity, List.class).getBody();
        for (Map<String, Object> champion : championMasterylist) {
            ChampionMastery championMastery = modelMapper.map(champion, ChampionMastery.class);
            championMastery.setId(championMastery.getSummonerId() + championMastery.getChampionId());
            championMasteryRepository.save(championMastery);
            summoner.getChampionMasteryList().add(championMastery);
        }
        return summonerRepository.save(summoner);
    }

    private Summoner updateBaseInfoSummoner(String name) {
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
