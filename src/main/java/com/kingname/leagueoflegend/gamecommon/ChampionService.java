package com.kingname.leagueoflegend.gamecommon;

import com.kingname.leagueoflegend.gamecommon.vo.ChampionRotation;
import com.kingname.leagueoflegend.common.Global;
import com.kingname.leagueoflegend.config.AppProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChampionService {

    private final RestTemplate restTemplate;
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
}
