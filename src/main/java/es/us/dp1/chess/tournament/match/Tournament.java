package es.us.dp1.chess.tournament.match;


import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import es.us.dp1.chess.tournament.model.BaseEntity;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinTable;

@Entity 
@Getter
@Setter
public class Tournament extends BaseEntity {

    @NotNull 
    @NotBlank 
    @Size(min = 3, max = 50)
    String name;

    @NotNull 
    @NotBlank 
    @Size(min = 3, max = 50)
    String location;

    @NotNull 
    LocalDateTime startDate;

    LocalDateTime endDate;

    @NotNull 
    @Min(2)
    Integer maxParticipants;

    @NotNull 
    @Size(min = 1)
    @OneToMany
    @JoinTable(name = "tournament_matches",
        joinColumns = @JoinColumn(name = "tournament_id"),
        inverseJoinColumns = @JoinColumn(name = "match_id")
    )
    List<ChessMatch> matches;
}
