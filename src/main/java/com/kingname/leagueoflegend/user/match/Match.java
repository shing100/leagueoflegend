package com.kingname.leagueoflegend.user.match;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@ToString @Setter @Getter
@EqualsAndHashCode(of = "gameId") @Builder
@NoArgsConstructor @AllArgsConstructor
public class Match {

    @Id
    private String gameId;
    private String platformId;
    private String champion;
    private int queue;
    private int season;
    private Date timestamp;
    private String role;
    private String lane;
}
