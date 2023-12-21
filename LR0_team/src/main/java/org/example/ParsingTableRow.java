package org.example;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ParsingTableRow {
    public enum ActionType { // enum representing different parsing action types
        SHIFT,
        REDUCE,
        ACCEPT,
        SHIFT_REDUCE_CONFLICT,
        REDUCE_REDUCE_CONFLICT
    }

    private int index; // index of the parsing state
    private ActionType action; // type of action associated with the parsing state
    private String reductionNonTerminal; // non-terminal to which reduction is applied
    private List<String> reductionSymbols = new ArrayList<>(); // what the reduction production contains
    private List<Pair<String, Integer>> shiftActions = new ArrayList<>(); // shifts associated with the parsing state


    public ParsingTableRow(){
        this.reductionSymbols = new ArrayList<>();
        this.shiftActions = new ArrayList<>();
    }


    // Method to generate a string representation of the reduction production
    public String getStringForReductionProd() {
        return this.reductionNonTerminal + " -> " + this.reductionSymbols;
    }


    // string representation of the parsing table row
    @Override
    public String toString() {
        StringBuilder row = new StringBuilder();

        row.append(index).append(": ")
                .append("action = ").append(action)
                .append(", reduction Non-Terminal = ").append(reductionNonTerminal)
                .append(", reduction symbols = ").append(reductionSymbols)
                .append(", shifts = ").append(shiftActions).append('\n').append('\n');

        return row.toString();
    }
}

