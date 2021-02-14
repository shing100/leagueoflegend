package com.kingname.leagueoflegend.main;

import com.kingname.leagueoflegend.user.summoner.Summoner;
import com.kingname.leagueoflegend.user.summoner.SummonerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {

    private final SummonerService summonerService;

    @GetMapping("/")
    public String home(Model model) {
        return "index";
    }

    @GetMapping("/profile")
    public String profile(@Param("username") String username, Model model) {
        log.info(username);
        if (username == null || "".equals(username)) {
            model.addAttribute("message", "소환사 이름을 정확히 입력해주세요");
            return "index";
        }
        Summoner summoner = summonerService.updateSummoner(username);
        model.addAttribute(summoner);
        return "profile/profile";
    }

    @GetMapping("/champion")
    public String champion(@Param("username") String username, Model model) {
        log.info(username);
        if (username == null || "".equals(username)) {
            model.addAttribute("message", "소환사 이름을 정확히 입력해주세요");
            return "index";
        }
        Summoner summoner = summonerService.getChampionMastery(username);
        model.addAttribute(summoner);
        return "profile/champion";
    }
}
