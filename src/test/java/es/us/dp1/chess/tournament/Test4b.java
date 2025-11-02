package es.us.dp1.chess.tournament;



import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import es.us.dp1.chess.tournament.match.Move;
import es.us.dp1.chess.tournament.match.Tournament;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;

@DataJpaTest()
public class Test4b extends ReflexiveTest{

    @Autowired(required = false)
    EntityManager em;

    @Test
    public void test4bTournamentMatchAnnotations() {
        checkThatFieldIsAnnotatedWith(Tournament.class, "matches", OneToMany.class);        
    }

    @Test
    public void test2MoveMatchAnnotations() {
        checkThatFieldIsAnnotatedWith(Move.class, "match", ManyToOne.class);        
        checkThatFieldIsAnnotatedWith(Move.class, "match", NotNull.class);        
    }    

    @Test
    public void test2MovePieceAnnotations() {        
        checkThatFieldIsAnnotatedWith(Move.class, "piece", ManyToOne.class);
        checkThatFieldIsAnnotatedWith(Move.class, "piece", NotNull.class);
    }    

    @Test
    public void test2MoveCaptureAnnotation() {
        checkThatFieldIsAnnotatedWith(Move.class, "capture", OneToOne.class);
        
    }    


}
