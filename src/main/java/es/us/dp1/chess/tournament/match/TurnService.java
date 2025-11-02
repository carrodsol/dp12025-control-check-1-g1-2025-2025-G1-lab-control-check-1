package es.us.dp1.chess.tournament.match;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.chess.tournament.exceptions.WrongUserAssignmentException;
import es.us.dp1.chess.tournament.user.User;


public class TurnService {

    TurnRepository turnRepository;

    public TurnService(TurnRepository turnRepository) {
        this.turnRepository = turnRepository;
    }

    public Turn getTurnById(Integer id) {
        return null; // TODO: Implement to solve exercise 5!
    }

    public List<Turn> getAll() {
        return null; // TODO: Implement to solve exercise 5!
    }
 
    public void save(Turn turn) throws WrongUserAssignmentException {
        // TODO: Implement to solve exercise 5!
    }



}
