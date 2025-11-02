package es.us.dp1.chess.tournament.match;

import java.util.List;
import java.util.Optional;

public interface TurnRepository {
    List<Turn> findAll();
    Turn save(Turn turn);
    Optional<Turn> findById(Integer id);
}
