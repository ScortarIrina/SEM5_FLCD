package org.example;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProgramInternalForm {

    private List<Pair<String, Pair<Integer, Integer>>> tokenPositionPair;

    private List<Integer> types;

    public ProgramInternalForm() {
        this.tokenPositionPair = new ArrayList<>();
        this.types = new ArrayList<>();
    }

    /**
     * We add a token/identifier/constant to its list + their position in the symbol table, and we also add the category
     * in the list of types
     *
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
            Pair<String, Pair<Integer, Integer>> pair = this.tokenPositionPair.get(i);
            Integer type = this.types.get(i);

            String token = pair.getFirst();
            Pair<Integer, Integer> position = pair.getSecond();

            computedString.append("Token/Identifier/Constant: ").append(token)
                    .append("\nPosition in Symbol Table: ").append(position)
                    .append("\nCategory: ").append(type).append("\n\n");
        }

        return computedString.toString();
    }
}
