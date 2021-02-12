package com.kingname.leagueoflegend.user.spectator;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class ActiveGame {

    private String gameId;
    private String mapId;
    private String gameMode;
    private String gameType;
    private String gameQueueConfigId;
    private List<Participants> participants;
}
