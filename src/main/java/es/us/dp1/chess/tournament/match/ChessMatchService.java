package es.us.dp1.chess.tournament.match;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import es.us.dp1.chess.tournament.exceptions.UnfeasibleMatchException;

import es.us.dp1.chess.tournament.user.User;

@Service
public class ChessMatchService {

    MatchRepository repo;    

    @Autowired
    public ChessMatchService(MatchRepository repo) {
        this.repo = repo;        
    }

    @Transactional(readOnly = true)
    public Optional<ChessMatch> getMatchById(Integer id) {
        return repo.findById(id);
    }

    @Transactional(rollbackFor =UnfeasibleMatchException.class)
    public ChessMatch save(ChessMatch m) {
        User winner = m.getWinner();
        if(winner != null && 
        (m.getBlackPlayer() == winner || m.getWhitePlayer() == winner) == false) {
            throw new UnfeasibleMatchException("No winner found in the match.");
        }
        if(m.getFinish() != null && m.getFinish().isBefore(m.getStart())) {
            throw new UnfeasibleMatchException("Finish date is before start date.");
        }
        return repo.save(m);
    }

    @Transactional(readOnly = true)
    public List<ChessMatch> getMatches() {
        return repo.findAll();
    }    
    

    

}
