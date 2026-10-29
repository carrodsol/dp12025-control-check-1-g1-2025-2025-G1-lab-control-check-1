package es.us.dp1.chess.tournament.match;


import es.us.dp1.chess.tournament.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import es.us.dp1.chess.tournament.exceptions.ResourceNotFoundException;
@RestController
@RequestMapping("/api/v1/matches")
public class ChessMatchController {

    UserService userService;
    ChessMatchService matchService;

    @Autowired 
    public ChessMatchController(ChessMatchService service,UserService userService){
        this.matchService=service;
        this.userService=userService;    
    }

    @GetMapping 
    public ResponseEntity<Iterable<ChessMatch>> getMatches() {
        return new ResponseEntity<>(matchService.getMatches(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ChessMatch> getMatchById(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(matchService.getMatchById(id).orElseThrow(() -> new ResourceNotFoundException("NOT FOUND")), HttpStatus.OK);
    }

}