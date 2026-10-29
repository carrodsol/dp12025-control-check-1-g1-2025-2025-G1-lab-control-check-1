package es.us.dp1.chess.tournament.match;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface MoveRepository extends CrudRepository<Move, Integer> {

    Optional<Move> findById(int i);
    
}
