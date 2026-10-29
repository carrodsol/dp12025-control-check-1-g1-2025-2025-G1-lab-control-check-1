import React, { useEffect, useState } from "react";
import { Table } from "reactstrap";

export default function MatchesListing() {
    const [matches, setMatches] = useState([]);
    useEffect(() => {
        fetch("/api/v1/matches")
            .then((response) => response.json())
            .then((data) => setMatches(data));
    }, []);
    // Check if matches is empty and display a message if it is
    return (
        <div>
            {matches.length === 0 && <h1>NO MATCHES</h1>}
        
                { matches.length > 0 && 
                <Table striped>
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Start</th>
                            <th>Finish</th>
                            <th>White</th>
                            <th>Black</th>
                            <th>Winner</th>
                        </tr>
                    </thead>
                    <tbody>
                        {matches.map((match) => (
                            <tr key={match.id}>
                                <td>{match.name}</td>
                                <td>{match.start}</td>
                                <td>{match.finish || "N/A"}</td>
                                <td>{match.whitePlayer.username}</td>
                                <td>{match.blackPlayer.username}</td>
                                <td>{match.winner ? match.winner.username : "N/A"}</td>
                            </tr>
                        ))}
                    </tbody>
                </Table>}
        </div>
    );
};
