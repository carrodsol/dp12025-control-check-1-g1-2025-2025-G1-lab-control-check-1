package es.us.dp1.chess.tournament.match;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface TournamentRepository extends CrudRepository<Tournament, Integer> {

    Optional<Tournament> findById(int i);
    
}
