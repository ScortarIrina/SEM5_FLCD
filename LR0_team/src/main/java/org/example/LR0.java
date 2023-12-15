package org.example;

import lombok.Data;

import java.util.*;

@Data
public class LR0 {
    private final Grammar grammar;
    private final Grammar workingGrammar;
    private List<Pair<String, List<String>>> productions;

    // Constructor for LR0 parser
    public LR0(Grammar grammar) throws Exception {
        this.grammar = grammar;

        // Check if the grammar is already enriched, else enrich it
        if (this.grammar.isGrammarEnriched()) {
            this.workingGrammar = this.grammar;
        } else {
            this.workingGrammar = this.grammar.makeGrammarEnriched();
        }

        // Get all individual productions
        productions = this.grammar.getAllIndividualProductions();
    }

    // Get the non-terminal before the dot in an LR0 item.
    public String getNonTerminalBeforeDot(Item item) {
        try {
            Integer dotLocation = item.getDotLocation();
            String possibleNonTerminal = item.getRightHandSide().get(dotLocation);

            // Return null if the non-terminal isn't found
            if (!grammar.getNonTerminals().contains(possibleNonTerminal)) {
                return null;
            }
            return possibleNonTerminal;
        } catch (Exception e) {
            return null;
        }
    }

    // Get the closure for an item
    public State closure(Item item) {
        Set<Item> oldClosure;
        Set<Item> currentClosure = Set.of(item); // Initialize the closure with the initial item

        // Compute closure until no more items can be added
        do {
            // Saving the current closure for comparison, in the old one
            oldClosure = currentClosure;

            // Creating a new set for the updated closure
            Set<Item> updatedClosure = new LinkedHashSet<>(currentClosure);

            // Iterating over each item in the current closure
            for (Item currentItem : currentClosure) {
                String nonTerminal = getNonTerminalBeforeDot(currentItem);

                if (nonTerminal != null) {
                    // For each non-terminal before the dot, we add items to the closure
                    for (List<String> production : grammar.getAllProductionsForNonTerminal(nonTerminal)) {
                        // Create a new LR0 item and add it to the closure
                        Item newClosureItem = new Item(nonTerminal, production, 0);
                        updatedClosure.add(newClosureItem);
                    }
                }
            }

            // Update the current closure with the updated one
            currentClosure = updatedClosure;

        } while (!oldClosure.equals(currentClosure)); // the loop is done after no more changes occur

        // Return a new state containing the final closure
        return new State(currentClosure);
    }

    // Method that moves the dot in front of a given symbol and calls closure for the new item
    // Returns a new state containing a list of states resulting from each computed closure
    public State goTo(State currentState, String symbolToLookFor) {
        Set<Item> resultItems = new LinkedHashSet<>();

        // loops through each LR(0) item in the current state
        for (Item currentItem : currentState.getItems()) {
            try {
                // Get the symbol after the dot in the current LR0 item
                String currentSymbol = currentItem.getRightHandSide().get(currentItem.getDotLocation());

                // Check if the symbol after the dot matches the symbol we are looking for
                if (Objects.equals(currentSymbol, symbolToLookFor)) {
                    // Move the dot after the symbol and call closure for the new item
                    int position = currentItem.getDotLocation() + 1;
                    Item newItemAfterMove = new Item(currentItem.getLeftHandSide(), currentItem.getRightHandSide(), position);
                    State newState = closure(newItemAfterMove);

                    // Add the items of the new state to the result
                    resultItems.addAll(newState.getItems());
                }
            } catch (Exception ignored) { // Ignore exception and continue iteration
            }
        }

        return new State(resultItems); // Returning the state with the final closure
    }

    // Compute and return the canonical collection
    public CanonicalCollection getCanonicalCollectionForGrammar() {
        CanonicalCollection collection = new CanonicalCollection();

        // Adding the closure of the initial item to the collection
        collection.addState(closure(new Item(workingGrammar.getStart(),
                workingGrammar.getAllProductionsForNonTerminal(workingGrammar.getStart()).get(0), 0)));


        // Go through each state from the collection and compute the closure for each symbol
        for (int i = 0; i < collection.getStates().size(); ++i) {
            for (String symbol : collection.getStates().get(i).getPartAfterTheDot()) {
                // Move the dot after each symbol and call closure for the new item
                State newState = goTo(collection.getStates().get(i), symbol);
                int newStateItemsLength = newState.getItems().size();

                if (newStateItemsLength != 0) {
                    // If the new state is not empty, it can be added
                    int newStateIndex = collection.getStates().indexOf(newState);
                    if (newStateIndex == -1) {
                        collection.addState(newState);
                    }
                }
            }
        }

        return collection;
    }
}