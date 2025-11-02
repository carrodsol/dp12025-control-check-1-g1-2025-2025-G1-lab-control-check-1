import { render, screen } from "../test-utils.jsx";
import { mockMatches } from "../mocks/handlers.js";
import {within } from '@testing-library/react'
import MatchesListing from "./index.js";

const getDataRows = (table) => {
    const allRows = within(table).getAllByRole("row");
    console.log("All rows count:", allRows.length);
    var dataRows=allRows.filter(
      (row) => within(row).queryAllByRole("columnheader").length === 0
    );
    console.log("Filterd rows count:", allRows.length);
    return dataRows;
  };

describe('Basic  lisitng of matches', () => {
        test('renders a table with data rows from the API (as many rows as mock data available)', async () => {
        render(<MatchesListing/>);

        // Wait until the table is in the DOM
        const table = await screen.findByRole("table");

       // Get all <tr> elements (no assumption about <tbody>)
        const rows = within(table).getAllByRole("row");
        // Filter out header rows — those containing “Name” or “Start”
        const dataRows =  getDataRows(table);            
        // Should have one row per match in mock data
        expect(dataRows.length).toBe(mockMatches.length)            
        
        
    });

     test("renders a table with the data of the  first mock match", async () => {
        render(<MatchesListing />);        
       // Wait until the table is in the DOM
        const table = await screen.findByRole("table");

        // Get all <tr> elements (no assumption about <tbody>)
        const allRows = within(table).getAllByRole("row");

        // Filter out header rows — those containing “Name” or “Start”
        const dataRows =  getDataRows(table);

        // Should have one row per match in mock data
        expect(dataRows.length).toBe(mockMatches.length);

        // Verify first row (basic data presence)
        const firstRow = dataRows[0];
        const cells = within(firstRow).getAllByRole("cell");        
        // 0 Name, 1 Start, 2 Finish, 3 White, 4 Black, 5 Winner
        expect(cells[0]).toHaveTextContent(new RegExp(mockMatches[0].name, "i"));
        expect(cells[1]).toHaveTextContent(mockMatches[0].start);        
        expect(cells[3]).toHaveTextContent(mockMatches[0].whitePlayer.username);
        expect(cells[4]).toHaveTextContent(mockMatches[0].blackPlayer.username);        
    });

    test("renders a table with the data of the last mock match", async () => {
        render(<MatchesListing />);        
       // Wait until the table is in the DOM
        const table = await screen.findByRole("table");

        // Get all <tr> elements (no assumption about <tbody>)
        const allRows = within(table).getAllByRole("row");

        // Filter out header rows — those containing “Name” or “Start”
        const dataRows = getDataRows(table);

        // Should have one row per match in mock data
        expect(dataRows.length).toBe(mockMatches.length);

        // Verify second row (has finish date and winner)
        const secondRow = dataRows[1];
        // Get all <td> cells in order
        const cells = within(secondRow).getAllByRole("cell");
        // 0 Name, 1 Start, 2 Finish, 3 White, 4 Black, 5 Winner
        expect(cells[0]).toHaveTextContent(new RegExp(mockMatches[1].name, "i"));
        expect(cells[1]).toHaveTextContent(mockMatches[1].start);   // Start
        expect(cells[2]).toHaveTextContent(mockMatches[1].finish);  // Finish
        expect(cells[3]).toHaveTextContent(mockMatches[1].whitePlayer.username);
        expect(cells[4]).toHaveTextContent(mockMatches[1].blackPlayer.username);
        expect(cells[5]).toHaveTextContent(mockMatches[1].winner.username);
    });
});