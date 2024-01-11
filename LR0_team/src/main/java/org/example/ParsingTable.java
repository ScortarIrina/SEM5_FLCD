package org.example;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class ParsingTable {
    public List<ParsingTableRow> elements; // the entries of the table will be saved in a list of 'ParsingTableRow' objects

    public ParsingTable() { this.elements = new ArrayList<>(); }


    @Override
    public String toString() {
        StringBuilder tableString = new StringBuilder("Parsing Table: \n");

        for (ParsingTableRow row : elements) {
            tableString.append(row).append('\n');
        }

        return tableString.toString();
    }

}
