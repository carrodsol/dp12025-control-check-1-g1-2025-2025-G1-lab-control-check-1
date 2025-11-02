package es.us.dp1.chess.tournament;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.us.dp1.chess.tournament.exceptions.UnfeasibleMatchException;
import es.us.dp1.chess.tournament.match.ChessBoard;
import es.us.dp1.chess.tournament.match.ChessMatch;
import es.us.dp1.chess.tournament.match.ChessMatchService;
import es.us.dp1.chess.tournament.match.MatchRepository;
import es.us.dp1.chess.tournament.user.Authorities;
import es.us.dp1.chess.tournament.user.User;


@ExtendWith(MockitoExtension.class)
public class Test1 extends ReflexiveTest{
    @Mock
    MatchRepository mr;

    ChessMatchService ms;

    @BeforeEach
    public void configuation(){
        ms=new ChessMatchService(mr);
    }

    @Test
    public void test1CheckTransactionalityOfMatchService(){
        checkTransactionalRollbackFor(ChessMatchService.class,"save", UnfeasibleMatchException.class,ChessMatch.class);        
    }    

     
    @Test
    public void test1MatchServiceCanSaveMatchesWithValidPlayersAndDates() throws UnfeasibleMatchException {        
        when(mr.save(any(ChessMatch.class))).thenReturn(null);
        User creator=createUser("creator");
        User opponent=createUser("opponent");                    
        ChessMatch m=new ChessMatch();        
        m.setWhitePlayer(creator);
        m.setBlackPlayer(opponent);
        m.setStart(LocalDateTime.of(2025, 10, 31, 0, 0, 0));
        m.setFinish(LocalDateTime.of(2025, 11, 2, 12, 0, 0));
        ChessBoard board=new ChessBoard();
        board.setCreatorTurn(true);
        m.setBoard(board);
        ms.save(m);
        // The test fails if the service does not invoke the save function of the repository:
        verify(mr).save(m);
    } 

    @Test
    public void test1MatchServiceCanSaveMatchesWithValidPlayersWithoutFinisDate() throws UnfeasibleMatchException {        
        when(mr.save(any(ChessMatch.class))).thenReturn(null);
        User creator=createUser("creator");
        User opponent=createUser("opponent");                    
        ChessMatch m=new ChessMatch();        
        m.setWhitePlayer(creator);
        m.setBlackPlayer(opponent);
        m.setWinner(opponent);
        m.setStart(LocalDateTime.of(2025, 10, 31, 0, 0, 0));
        m.setFinish(null);
        ChessBoard board=new ChessBoard();
        board.setCreatorTurn(true);
        m.setBoard(board);
        ms.save(m);
        // The test fails if the service does not invoke the save function of the repository:
        verify(mr).save(m);
    } 

    @Test
    public void test1MatchServiceCanNotSaveMatchessWithInvalidPlayers() throws UnfeasibleMatchException {
        assertNotNull(ms,"TurnService was not injected by spring");
        lenient().when(mr.save(any(ChessMatch.class))).thenReturn(null);
        User creator=createUser("creator");
        User opponent=createUser("opponent");                    
        User spontaneous=createUser("spontaneous");                    
        ChessMatch m=new ChessMatch();        
        m.setWhitePlayer(creator);
        m.setBlackPlayer(opponent);
        m.setWinner(spontaneous);
        m.setStart(LocalDateTime.of(2025, 10, 31, 0, 0, 0));
        m.setFinish(LocalDateTime.of(2025, 11, 2, 12, 0, 0));
        ChessBoard board=new ChessBoard();
        board.setCreatorTurn(true);
        m.setBoard(board);
        // The test fails if the service does not throw the exception;
        assertThrows(UnfeasibleMatchException.class, ()->{
            ms.save(m);
        });                
        // The test fails if the service invokes the save function of the repository:
        verify(mr,never()).save(m);
    } 

    @Test
    public void test1MatchesServiceCanNotSaveMatchesWithInvalidDates() throws UnfeasibleMatchException {
        assertNotNull(ms,"TurnService was not injected by spring");
        lenient().when(mr.save(any(ChessMatch.class))).thenReturn(null);
        User creator=Test1.createUser("creator");
        User opponent=Test1.createUser("opponent");                
        ChessMatch m=new ChessMatch();        
        m.setWhitePlayer(creator);
        m.setWhitePlayer(opponent);
        // Warning!!!! The finis date is before the start date!
        m.setFinish(LocalDateTime.of(2025, 10, 31, 0, 0, 0));
        m.setStart(LocalDateTime.of(2025, 11, 2, 12, 0, 0));
        m.setWinner(creator);        
        ChessBoard board=new ChessBoard();
        board.setCreatorTurn(false);
        m.setBoard(board);
        // The test fails if the service does not throw the exception;
        assertThrows(UnfeasibleMatchException.class, ()->{
            ms.save(m);
        });                
        // The test fails if the service invokes the save function of the repository:
        verify(mr,never()).save(m);
    } 


    public static User createUser(String name){
        Authorities a1=new Authorities();
        a1.setAuthority("PANA");
        User u1=new User();
        setValue(u1,"username",String.class,name);
        setValue(u1, "authority", Authorities.class, a1);
        return u1;
    }
}
