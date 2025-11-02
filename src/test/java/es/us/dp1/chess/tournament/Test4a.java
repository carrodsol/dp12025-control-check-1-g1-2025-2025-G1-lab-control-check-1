package es.us.dp1.chess.tournament;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import es.us.dp1.chess.tournament.match.TournamentRepository;
import es.us.dp1.chess.tournament.match.ChessBoard;
import es.us.dp1.chess.tournament.match.ChessMatch;
import es.us.dp1.chess.tournament.match.Move;
import es.us.dp1.chess.tournament.match.MoveRepository;
import es.us.dp1.chess.tournament.match.Piece;
import es.us.dp1.chess.tournament.match.PieceColor;
import es.us.dp1.chess.tournament.match.PieceType;
import es.us.dp1.chess.tournament.match.Tournament;
import es.us.dp1.chess.tournament.user.Authorities;
import es.us.dp1.chess.tournament.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;

@DataJpaTest()
public class Test4a extends ReflexiveTest{

    @Autowired(required = false)
    TournamentRepository TournamentRepository;

    @Autowired(required = false)
    MoveRepository MoveRepository;

    @Autowired
    EntityManager em;

    @Test
    public void test1RepositoriesExist(){
        assertNotNull(MoveRepository,"The Move repository was not injected into the tests, its autowired value was null");
        assertNotNull(TournamentRepository,"The Tournament repository was not injected into the tests, its autowired value was null");

        test1MoveRepositoriesContainsMethod();
        test1TournamentRepositoryContainsMethod();
    }

    public void test1MoveRepositoriesContainsMethod(){
        if(MoveRepository!=null){
            Object v=MoveRepository.findById(12);
            assertFalse(null!=v && ((Optional)v).isPresent(), "No result (null) should be returned for a Move that does not exist");
        }else
            fail("The Move repository was not injected into the tests, its autowired value was null");
    }

    public void test1TournamentRepositoryContainsMethod(){
        if(TournamentRepository!=null){
            Object v=TournamentRepository.findById(12);
            assertFalse(null!=v && ((Optional)v).isPresent(), "No result (null) should be returned for a Tournament that does not exist");
        }else
            fail("The Tournament repository was not injected into the tests, its autowired value was null");
    }




    @Test
    public void test1CheckTournamentConstraints() {
         Map<String,List<Object>> invalidValues=Map.of(
                                            "name",     List.of(
                                                    "      ",
                                                    "a",                                                    
                                                    "En un lugar de la Mancha, de cuyo nombre no quiero acordarme, no ha mucho tiempo que vivía un hidalgo de los de lanza en astillero, adarga antigua, rocín flaco y galgo corredor. Una olla de algo más vaca que carnero, salpicón las más noches, duelos y quebrantos los sábados, lentejas los viernes, algún palomino de añadidura los domingos, consumían las tres partes de su hacienda. "),
                                            "location", List.of("      ",
                                                    "a",                                                    
                                                    "En un lugar de la Mancha, de cuyo nombre no quiero acordarme, no ha mucho tiempo que vivía un hidalgo de los de lanza en astillero, adarga antigua, rocín flaco y galgo corredor. Una olla de algo más vaca que carnero, salpicón las más noches, duelos y quebrantos los sábados, lentejas los viernes, algún palomino de añadidura los domingos, consumían las tres partes de su hacienda. "                                                    
                                                    ),
                                                "maxParticipants", List.of(0, -5)
                                            );

        Tournament s=createValidTournament(em);
        em.persist(s);

        checkThatFieldsAreMandatory(s, em, "name","location","startDate","maxParticipants");

        checkThatValuesAreNotValid(s, invalidValues,em);
    }


    @Test
    public void test1CheckMoveContraints() {
         Map<String,List<Object>> invalidValues=Map.of(
                                            "originX", List.of(-1,9),
                                            "originY", List.of(-1,9),
                                            "destinationX", List.of(-1,9),
                                            "destinationY", List.of(-1,9),
                                            "turn", List.of(-1,0)                                                
                                            );

        Move t=createValidMove(em);
        em.persist(t);

        checkThatFieldsAreMandatory(t, em, "originX","originY","destinationX","destinationY","turn");

        checkThatValuesAreNotValid(t, invalidValues,em);
    }

    @Test
    public void test1CheckTournamentAnnotations() throws NoSuchFieldException, SecurityException {
        assertTrue(classIsAnnotatedWith(Tournament.class,Entity.class));        
    }
    @Test
    public void test1CheckMoveAnnotations() throws NoSuchFieldException, SecurityException {
        assertTrue(classIsAnnotatedWith(Move.class,Entity.class));        
    }


    public static Move createValidMove(EntityManager em){
        Move move = new Move();
        ChessMatch m=null;
        Piece p=null;        
        if(em!=null){
            m=em.find(ChessMatch.class, 1);
            p=em.find(Piece.class,2);
        }else{
            m=createMatch();
            p=createPiece(m);
        }        
        move.setOriginX(2);
        move.setOriginY(2);
        move.setDestinationX(3);
        move.setDestinationY(3);
        move.setPiece(p);
        move.setCapture(null);              
        move.setMatch(m);
            
        return move;
    }

    private static Piece createPiece(ChessMatch m) {
        Piece piece=new Piece();
        piece.setBoard(m.getBoard());
        piece.setColor(PieceColor.BLACK);
        PieceType type=new PieceType();
        type.setName("Pawn");       
        piece.setType(type);
        piece.setCaptured(false);        
        return piece;
    }

    private static ChessMatch createMatch() {
        ChessMatch match=new ChessMatch();
        match.setName("Test Match");
        match.setStart( LocalDateTime.of(2025, 11, 1, 12, 0));
        match.setFinish( LocalDateTime.of(2025, 11, 1, 13, 0));
        match.setTurnDuration(60L);
        match.setWhitePlayer(createUser("José Maestre"));
        match.setBlackPlayer(createUser("Gary Kasparov"));
        match.setWinner(null);
        match.setBoard(new ChessBoard());
        return match;
    }

    public static Tournament createValidTournament(EntityManager em){
        Tournament tournament=new Tournament();
        tournament.setName( "Autumn Open Tournament");
        tournament.setLocation("Wherever");
        tournament.setStartDate( LocalDateTime.of(2025, 10, 20, 12, 00));
        tournament.setEndDate( LocalDateTime.of(2025, 10, 27, 18, 00));
        tournament.setMaxParticipants(100); 
        return tournament;
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
