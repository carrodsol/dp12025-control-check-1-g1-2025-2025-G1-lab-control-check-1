package es.us.dp1.chess.tournament.match;

import java.time.LocalDateTime;

import es.us.dp1.chess.tournament.strategy.Strategy;
import es.us.dp1.chess.tournament.user.User;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Transient;

@Getter
@Setter
public class Turn {
    
    Integer turnNumber;

    LocalDateTime startTime;
    
    LocalDateTime endTime;

    @Transient
    User player;

    @Transient
    Strategy strategy;

    @Transient
    ChessMatch match;
}
