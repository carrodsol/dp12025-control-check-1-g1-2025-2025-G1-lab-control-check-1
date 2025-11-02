package es.us.dp1.chess.tournament;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import es.us.dp1.chess.tournament.match.Move;
import es.us.dp1.chess.tournament.match.Tournament;
import jakarta.persistence.EntityManager;

@DataJpaTest()
public class Test5 extends ReflexiveTest {

    @Autowired
    EntityManager em;

    // ---------- Tournaments ----------
    @Test
    public void test5aInitialTournament1() {
        Tournament t = em.find(Tournament.class, 1);
        assertNotNull(t, "There should exist a Tournament with id:1");

        assertEquals("Abierto de Ajedrez Villa de Los Palacios y Vfca.",
                t.getName());
        assertEquals("Circulo Joven, Los Palacios y Villafranca",
                t.getLocation());
        assertEquals(LocalDateTime.of(2024, 11, 10,9,0,0),
                t.getStartDate());
        assertEquals(LocalDateTime.of(2024, 11, 12,17,0,0),
                t.getEndDate());
    }

    @Test
    public void test5aInitialTournament2() {
        Tournament t = em.find(Tournament.class, 2);
        assertNotNull(t, "There should exist a Tournament with id:2");

        assertEquals("Torneo en Honor de José Maestre Amuedo",
               t.getName());
        assertEquals("Club cultural El Casino",
                t.getLocation());
        assertEquals(LocalDateTime.of(1990, 9, 17,17,0,0),
                t.getStartDate());
        assertEquals(LocalDateTime.of(1990, 9, 20,21,0,0),
                t.getEndDate()); 
    }

    // ---------- Moves ----------

    @Test
    public void test5aInitialMove1() {
        Move m = em.find(Move.class, 1);
        assertNotNull(m, "There should exist a Move with id:1");

        assertEquals(1, m.getTurn());

        assertEquals(4, m.getOriginX());
        assertEquals(2, m.getOriginY());
        assertEquals(4, m.getDestinationX());
        assertEquals(4, m.getDestinationY());
    }

    @Test
    public void test5aInitialMove2() {
        Move m = em.find(Move.class, 2);
        assertNotNull(m, "There should exist a Move with id:2");

        assertEquals(7,m.getTurn());

        assertEquals(7, m.getOriginX());
        assertEquals(5, m.getOriginY());
        assertEquals(5, m.getDestinationX());
        assertEquals(6, m.getDestinationY());
    }
    
    // -------------------------
    // Tournaments -> Matches
    // -------------------------

    @Test
    public void test5bTournamentMatchesLinks() {        
        String matchesGetter = "getMatches";

        // Tournament 1 -> contains match 15
        checkContainsById(Tournament.class, 1, matchesGetter, 15, em);

        // Tournament 2 -> contains matches 5 and 6
        checkContainsById(Tournament.class, 2, matchesGetter, 5, em);
        checkContainsById(Tournament.class, 2, matchesGetter, 6, em);
    }

    // -------------------------
    // Moves -> Match
    // -------------------------

    @Test
    public void test5bMoveToMatchLinks() {
        
        String matchGetter = "getMatch";

        // Move 1 -> match 15
        checkLinkedById(Move.class, 1, matchGetter, 15, em);

        // Move 2 -> match 5
        checkLinkedById(Move.class, 2, matchGetter, 5, em);
    }


    // -------------------------
    // Moves -> Piece (moved piece)
    // -------------------------

    @Test
    public void test5bMoveToPieceLinks() {        
        String pieceGetter = "getPiece";

        // Move 1 -> piece 131
        checkLinkedById(Move.class, 1, pieceGetter, 131, em);

        // Move 2 -> piece 5001
        checkLinkedById(Move.class, 2, pieceGetter, 5001, em);
    }

    // -------------------------
    // Moves -> Captured piece (if any)
    // -------------------------

    @Test
    public void test5bMoveToCapturedPieceLinks() {        
        String capturedGetter = "getCapture";

        // Move 2 -> captured piece 5002
        checkLinkedById(Move.class, 2, capturedGetter, 5002, em);

        // Optional sanity-check: Move 1 should have no captured piece (according to the spec)
        Object move1 = em.find(Move.class, 1);
        assertNotNull(move1, "There should exist a Move with id:1");
        Object captured1 = invokeMethodReflexively(move1, capturedGetter);
        assertNull(captured1, "Move id=1 should not have a captured piece");
    }
   
}