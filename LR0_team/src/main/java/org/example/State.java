package org.example;

import lombok.Data;

import java.util.*;

@Data
public class State {
    private Set<Item> items; // The state contains multiple LR items -> all items corresponding to same live prefix

    public State(Set<Item> items) {
        this.items = items;
    }

    // Retrieving the symbols after the dot in LR(0) items
    public List<String> getPartAfterTheDot() {
        Set<String> symbols = new LinkedHashSet<>(); // Using a Set to collect unique symbols

        for (Item item : items) { // Iterating over each item in the collection

            List<String> rhs = item.getRightHandSide();
            Integer dotPos = item.getDotLocation();

            // Check if the dot is not at the end of the right-hand side
            if (dotPos < rhs.size()) {
                // Add the symbol after the dot to the set
                String newSymbol = rhs.get(dotPos);
                symbols.add(newSymbol);
            }
        }

        return new ArrayList<>(symbols); // Convert the set to a list and return
    }
}
