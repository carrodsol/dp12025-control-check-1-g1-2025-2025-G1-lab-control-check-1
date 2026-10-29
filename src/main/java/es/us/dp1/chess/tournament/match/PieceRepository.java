package es.us.dp1.chess.tournament.match;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface PieceRepository extends CrudRepository<Piece, Integer> {

    List<Piece> findByColorAndTypeName(PieceColor color, String typeName);

    List<Piece> findAll();

    Optional<Piece> findById(Integer i);

    Piece save(Piece piece);
    
}
