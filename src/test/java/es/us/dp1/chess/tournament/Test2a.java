
package es.us.dp1.chess.tournament;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.annotation.FilterType;

import es.us.dp1.chess.tournament.match.Piece;
import es.us.dp1.chess.tournament.match.PieceColor;
import es.us.dp1.chess.tournament.match.PieceRepository;
import es.us.dp1.chess.tournament.match.PieceService;
import jakarta.persistence.EntityManager;

@ExtendWith(MockitoExtension.class)
@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = PieceService.class))
public class Test2a {

    @Autowired
    PieceRepository pr;

    @Autowired PieceService ms;

    @Autowired
    EntityManager em;
        

    @Test
    void test2aPieceServiceIsAnnotatedWithService() {
        assertTrue(PieceService.class.isAnnotationPresent(Service.class),
                "PieceService must be annotated with @Service");
    }       

    @Test
    void test2aPieceServiceClassShouldNotBeAnnotatedWithTransactional() {
        assertFalse(PieceService.class.isAnnotationPresent(Transactional.class),
                "@Transactional must not be applied at class level");
    }

    @Test
    void test2aRequiredPublicMethodsOfPieceServiceShouldBeTransactional() throws Exception {
        Method m1 = PieceService.class.getMethod("getPiecesByColorAndType", PieceColor.class, String.class);
        Method m2 = PieceService.class.getMethod("getAll");
        Method m3 = PieceService.class.getMethod("getById", Integer.class);
        Method m4 = PieceService.class.getMethod("save", Piece.class);

        List<Method> methods = List.of(m1, m2, m3, m4);
        for (Method m : methods) {
            assertTrue(m.isAnnotationPresent(Transactional.class),
                    "Method " + m.getName() + " must be annotated with @Transactional");
        }
    }

    @Test
    void teste2aPieceRepositoryShouldBeASpringDataRepository() {
        boolean isSpringDataRepo =
                Repository.class.isAssignableFrom(PieceRepository.class) ||
                JpaRepository.class.isAssignableFrom(PieceRepository.class);

        assertTrue(isSpringDataRepo,
                "PieceRepository must be a Spring Data repository (e.g., extend JpaRepository)");

        // Optional: check for correct generic parameters <Piece, Long>
        if (JpaRepository.class.isAssignableFrom(PieceRepository.class)) {
            boolean genericsOk = false;
            for (var i : PieceRepository.class.getGenericInterfaces()) {
                if (i instanceof ParameterizedType pt &&
                    pt.getRawType() instanceof Class<?> raw &&
                    JpaRepository.class.isAssignableFrom(raw)) {
                    var args = pt.getActualTypeArguments();
                    if (args.length == 2 &&
                        args[0].getTypeName().endsWith(".Piece") &&
                        (args[1].getTypeName().endsWith(".Integer") ||
                         args[1].getTypeName().equals("java.lang.Integer"))) {
                        genericsOk = true; break;
                    }
                }
            }
            assertTrue(genericsOk, "JpaRepository must be parameterized as JpaRepository<Piece, Long>");
        }
    }

     @Test
    void test2aPieceServiceGetPiecesByColorAndTypeDelegatesToRepository() {
        PieceRepository repository = mock(PieceRepository.class);
        PieceService service = new PieceService(Optional.of(repository));
        PieceColor color = PieceColor.valueOf("WHITE");
        String typeName = "PAWN";

        var p1 = mock(Piece.class);
        var p2 = mock(Piece.class);
        when(repository.findByColorAndTypeName(color, typeName))
                .thenReturn(List.of(p1, p2));

        List<Piece> result = service.getPiecesByColorAndType(color, typeName);

        assertEquals(2, result.size());
        verify(repository, times(1)).findByColorAndTypeName(color, typeName);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void test2aPieceServiceGetAllDelegatesToRepository() {
        PieceRepository repository = mock(PieceRepository.class);
        PieceService service = new PieceService(Optional.of(repository));
        var p1 = mock(Piece.class);
        when(repository.findAll()).thenReturn(List.of(p1));

        List<Piece> result = service.getAll();

        assertEquals(1, result.size());
        verify(repository).findAll();
        verifyNoMoreInteractions(repository);
    }

    @Test
    void test2aPieceServiceGetByIdDelegatesToRepository() {
        PieceRepository repository = mock(PieceRepository.class);
        PieceService service = new PieceService(Optional.of(repository));
        var piece = mock(Piece.class);
        when(repository.findById(42)).thenReturn(Optional.of(piece));

        Piece result = service.getById(42);

        assertTrue(result!=null );
        assertSame(piece, result);
        verify(repository).findById(42);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void test2aPieceServiceSaveDelegatesToRepository() {
        PieceRepository repository = mock(PieceRepository.class);
        PieceService service = new PieceService(Optional.of(repository));
        var piece = mock(Piece.class);
        when(repository.save(piece)).thenReturn(piece);

        var saved = service.save(piece);

        assertSame(piece, saved);
        verify(repository).save(piece);
        verifyNoMoreInteractions(repository);
    }

    @Test    
    void test2aPieceRepositoryFindWhitePawns() {
        List<Piece> whitePawns = pr.findByColorAndTypeName(PieceColor.WHITE, "PAWN");

        // From data.sql: eight WHITE Pawns 
        assertEquals(8, whitePawns.size(), "Should return exactly eight WHITE PAWNs");

        // Defensive checks: all returned pieces match the filter
        assertTrue(whitePawns.stream().allMatch(p ->
                p.getColor() == PieceColor.WHITE &&
                p.getType() != null &&
                "PAWN".equals(p.getType().getName())),
                "All returned pieces must be WHITE and of type PAWN");
    }

    @Test
    @DisplayName("findByColorAndTypeName: returns only BLACK Pawns (captured and not captured)")
    void test2aFindBlackPawns() {
        List<Piece> blackPawns = pr.findByColorAndTypeName(PieceColor.BLACK, "PAWN");

        // From data.sql: two BLACK Pawns (one captured, one not)
        assertEquals(8, blackPawns.size(), "Should return exactly eight BLACK PAWNs");

        // Validate payload correctness
        assertTrue(blackPawns.stream().allMatch(p ->
                p.getColor() == PieceColor.BLACK &&
                p.getType() != null &&
                "PAWN".equals(p.getType().getName())),
                "All returned pieces must be BLACK and of type PAWN");
    }

    @Test
    @DisplayName("findByColorAndTypeName: returns exactly three WHITE KINGs")
    void test2aFindWhiteKings() {
        // There are no WHITE Knights in data.sql
        List<Piece> result = pr.findByColorAndTypeName(PieceColor.WHITE, "KING");
        assertEquals(3,result.size(), "Should return the three WHITE KINGs");
    }

    @Test
    @DisplayName("findByColorAndTypeName: returns empty list when no match")
    void test2aNoResultsForMismatchedFilter() {
        // There are no zelda amibos in data.sql
        List<Piece> result = pr.findByColorAndTypeName(PieceColor.WHITE, "Zelda Amibo");
        assertTrue(result.isEmpty(), "Should return empty list when there is no match");
    }
    
}
