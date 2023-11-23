package org.example;

import java.util.ArrayList;
import java.util.List;

public class ProgramInternalForm {

    private final List<Pair<String, Pair<Integer, Integer>>> tokenPositionPair;

    private final List<Integer> types;

    public ProgramInternalForm() {
        this.tokenPositionPair = new ArrayList<>();
        this.types = new ArrayList<>();
    }

    /**
     * We add a token/identifier/constant to its list + their position in the symbol table, and we also add the category
     * in the list of types
     * @param pair - pair composed of token/constant/identifier + its position in the ST
     * @param type - the category of the token (2, 3, 4) or constant (0) or identifier (1)
     */
    public void addToPIF(Pair<String, Pair<Integer, Integer>> pair, Integer type) {
        this.tokenPositionPair.add(pair);
        this.types.add(type);
    }

    @Override
    public String toString() {
        StringBuilder computedString = new StringBuilder();
        for (int i = 0; i < this.tokenPositionPair.size(); i++) {
            computedString.append(this.tokenPositionPair.get(i).getFirst())
                    .append(" - ")
                    .append(this.tokenPositionPair.get(i).getSecond())
                    .append(" -> ")
                    .append(types.get(i))
                    .append("\n");
        }

        return computedString.toString();
    }
}
