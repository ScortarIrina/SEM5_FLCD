package org.example;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ParsingTableRow {
    private int index; // index of the parsing state
    private StateActionType action; // type of action associated with the parsing state
    private String reductionNonTerminal; // non-terminal to which reduction is applied
    private List<String> reductionSymbols; // what the reduction production contains
    private List<Pair<String, Integer>> shiftActions; // shifts associated with the parsing state


    public ParsingTableRow() {
        this.reductionSymbols = new ArrayList<>();
        this.shiftActions = new ArrayList<>();
    }


    // Method to generate a string representation of the reduction production
    public String getStringForReductionProd() {
        return this.reductionNonTerminal + " -> " + this.reductionSymbols;
    }


    // string representation of the parsing table row
    @Override
    public String toString(){
        return "Row: " +
                "stateIndex= " + index +
                ", action='" + action + '\'' +
                ", reduceNonTerminal='" + reductionNonTerminal + '\'' +
                ", reduceContent = " + reductionSymbols +
                ", shifts = " + shiftActions;
    }
}

