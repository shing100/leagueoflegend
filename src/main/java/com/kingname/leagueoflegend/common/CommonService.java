package com.kingname.leagueoflegend.common;

import com.kingname.leagueoflegend.common.champion.ChampionRotation;
import com.kingname.leagueoflegend.common.player.Player;
import com.kingname.leagueoflegend.config.AppProperties;
import com.kingname.leagueoflegend.util.Global;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonService {

    private final RestTemplate restTemplate;
    private final ModelMapper modelMapper;
    private final AppProperties appProperties;

    public ChampionRotation getChampionList() {
        // TODO 서버에 저장하기
        return getUrlChampionList();
    }

    public ChampionRotation getUrlChampionList() {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(appProperties.getChampion());
        HttpEntity<Object> httpEntity = setHttpContext();
        ChampionRotation body = restTemplate.exchange(uriComponentsBuilder.toUriString(), HttpMethod.GET, httpEntity, ChampionRotation.class).getBody();
        body.setCreateDt(LocalDateTime.now());
        return body;
    }

    private HttpEntity<Object> setHttpContext() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(Global.X_Riot_Token, appProperties.getApikey());
        return new HttpEntity<>(httpHeaders);
    }

    public List<Player> getLolAsiaRankList() {
        List<Player> ranks = new ArrayList();
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(appProperties.getLeaderboards());
        HttpEntity<Object> httpEntity = setHttpContext();
        Map<String, List<Map<String, Object>>> rankList = restTemplate.exchange(uriComponentsBuilder.toUriString(), HttpMethod.GET, httpEntity, Map.class).getBody();
        List<Map<String, Object>> players = rankList.get("players");
        for (Map<String, Object> player : players) {
            Player ranker = modelMapper.map(player, Player.class);
            ranks.add(ranker);
        }
        return ranks;
    }
}
