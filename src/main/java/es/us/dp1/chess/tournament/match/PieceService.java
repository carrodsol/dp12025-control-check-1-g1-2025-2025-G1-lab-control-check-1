package es.us.dp1.chess.tournament.match;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service 
public class PieceService {

    PieceRepository repository;

    @Autowired 
    public PieceService(Optional<PieceRepository> repository) {
        this.repository = repository.isPresent()?repository.get():null;
    }
    @Transactional 
    public List<Piece> getPiecesByColorAndType(PieceColor color, String typeName) {
        return repository.findByColorAndTypeName(color, typeName);
    }

    @Transactional(readOnly = true)
    public List<Piece> getAll() {
        
        return repository.findAll();
    }
    @Transactional(readOnly = true)
    public Piece getById(Integer i) { 
        return repository.findById(i).orElse(null);
    }
    @Transactional 
    public Piece save(Piece piece) {
        return repository.save(piece);
    }
    
}
