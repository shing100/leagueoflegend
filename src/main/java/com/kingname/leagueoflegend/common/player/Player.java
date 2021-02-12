package com.kingname.leagueoflegend.common.player;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Player {

    private String name;
    private int rank;
    private int lp;

}
