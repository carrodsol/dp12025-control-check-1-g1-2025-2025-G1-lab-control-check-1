package es.us.dp1.chess.tournament.match;

import jakarta.persistence.Entity;

import lombok.Getter;
import es.us.dp1.chess.tournament.model.BaseEntity;
import lombok.Setter;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity 
@Getter
@Setter
public class Move extends BaseEntity {

    @NotNull
    @Min(1)
    Integer turn;

    @NotNull
    @Min(1)
    @Max(8)
    Integer originX;

    @NotNull
    @Min(1)
    @Max(8)
    Integer originY;

    @NotNull
    @Min(1)
    @Max(8)
    Integer destinationX;

    @NotNull
    @Min(1)
    @Max(8)
    Integer destinationY;

    @NotNull
    @ManyToOne
    ChessMatch match;

    @NotNull 
    @ManyToOne
    Piece piece;

    @OneToOne
    Piece capture;
}
