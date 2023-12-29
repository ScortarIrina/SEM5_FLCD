package org.example;

import lombok.Data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
        int index = 0;
        while (index < collection.getStates().size()) {
            for (String symbol : collection.getStates().get(index).getPartAfterTheDot()) {
                State newState = goTo(collection.getStates().get(index), symbol);
                if (!newState.getItems().isEmpty()) {
                    int indexState = collection.getStates().indexOf(newState);
                    if (indexState == -1) {
                        collection.addState(newState);
                        indexState = collection.getStates().size() - 1;
                    }
//                    System.out.println("(" + index + ", " + symbol + ") -> " + indexState);
                    collection.connectStates(index, symbol, indexState);
                }
            }
            ++index;
        }
        return collection;
    }

    public ParsingTable getParsingTable(CanonicalCollection canonicalCollection) throws Exception {
        ParsingTable parsingTable = new ParsingTable();

        for (int i = 0; i < this.getCanonicalCollectionForGrammar().getStates().size(); i++) {

            State state = this.getCanonicalCollectionForGrammar().getStates().get(i);

            // We create a new row entry for our parsing table
            ParsingTableRow row = new ParsingTableRow();

            // We set the number of the state (the index)
            row.setIndex(i);


            // We set the action of the state (SHIFT/REDUCE/ACCEPT)
            row.setAction(state.getStateActionType());

            // We initialize the shifts list, in case there will be the case to add them.
            row.setShiftActions(new ArrayList<>());

            // If we have any of the two conflicts, we display the state and the symbol and stop the algorithm
            // If we have conflicts we cant go further
            if (state.getStateActionType() == StateActionType.SHIFT_REDUCE_CONFLICT || state.getStateActionType() == StateActionType.REDUCE_REDUCE_CONFLICT) {
                for (Map.Entry<Pair<Integer, String>, Integer> e2 : canonicalCollection.getAdjacencyList().entrySet()) {
                    Pair<Integer, String> k2 = e2.getKey();
                    Integer v2 = e2.getValue();

                    if (v2.equals(row.getIndex())) {
                        System.out.println("STATE INDEX -> " + row.getIndex());
                        writeToFile("src/main/java/org/example/IO/out2.txt", "STATE INDEX -> " + row.getIndex());
                        System.out.println("SYMBOL -> " + k2.getSecondElement());
                        writeToFile("src/main/java/org/example/IO/out2.txt", "SYMBOL -> " + k2.getSecondElement());
                        System.out.println("INITIAL STATE -> " + k2.getFirstElement());
                        writeToFile("src/main/java/org/example/IO/out2.txt", "INITIAL STATE -> " + k2.getFirstElement());
                        System.out.println("( " + k2.getFirstElement() + ", " + k2.getSecondElement() + " )" + " -> " + row.getIndex());
                        writeToFile("src/main/java/org/example/IO/out2.txt", "( " + k2.getFirstElement() + ", " + k2.getSecondElement() + " )" + " -> " + row.getIndex());
                        System.out.println("STATE -> " + state);
                        writeToFile("src/main/java/org/example/IO/out2.txt", "STATE -> " + state);

                        break;
                    }
                }
                parsingTable.setElements(new ArrayList<>());
                return parsingTable;

                // If the action is REDUCE, it means the state has only one item, which has the dot at the end
                // So we set the reduceNonTerminal, which is the left hand side and also set the reduce content which
                // is the right hand side
            } else if (state.getStateActionType() == StateActionType.REDUCE) {
                Item item = state.getItems().stream().filter(Item::dotIsLast).findAny().orElse(null);
                if (item != null) {
                    row.setShiftActions(null);
                    row.setReductionNonTerminal(item.getLeftHandSide());
                    row.setReductionSymbols(item.getRightHandSide());
                } else {
                    throw new Exception("How did you even get here?");
                }
                // If the action is ACCEPT, we just initialize all the other left fields with null, because the action was
                // set in the beginning
            } else if (state.getStateActionType() == StateActionType.ACCEPT) {
                row.setReductionSymbols(null);
                row.setReductionNonTerminal(null);
                row.setShiftActions(null);
                // If the action is SHIFT, we need to look for all the new states that are created from the intial
                // state and add them to the shifts list
            } else if (state.getStateActionType() == StateActionType.SHIFT) {

                List<Pair<String, Integer>> goTos = new ArrayList<>();

                for (Map.Entry<Pair<Integer, String>, Integer> entry : canonicalCollection.getAdjacencyList().entrySet()) {

                    Pair<Integer, String> key = entry.getKey();
                    if (key.getFirstElement() == row.getIndex()) {
                        goTos.add(new Pair<>(key.getSecondElement(), entry.getValue()));
                    }
                }

                row.setShiftActions(goTos);
                row.setReductionSymbols(null);
                row.setReductionNonTerminal(null);
            }

            parsingTable.getElements().add(row);
        }

        return parsingTable;
    }


    /**
     * With this method we parse the input sequence and find if the sequence is accepted by the grammar or not
     *
     * @param inputStack   - the input stack which contains actually all the elements from the sequence
     * @param parsingTable - the parsing table which we will use in order to parse
     * @param filePath     - the file path where we will display the parse result
     * @throws IOException - in case of input output exception for writing/reading the file
     */
    public void parse(Stack<String> inputStack, ParsingTable parsingTable, String filePath) throws IOException {
        Stack<Pair<String, Integer>> workingStack = new Stack<>();
        Stack<String> outputStack = new Stack<>();
        Stack<Integer> outputNumberStack = new Stack<>();

        String lastSymbol = "";
        int stateIndex = 0;

        boolean sem = true;

        workingStack.push(new Pair<>(lastSymbol, stateIndex));
        ParsingTableRow lastRow = null;
        String onErrorSymbol = null;

        try {
            do {
                if (!inputStack.isEmpty()) {
                    // We keep the symbol before which an error might occur
                    onErrorSymbol = inputStack.peek();
                }
                // We update the last row from the table we worked with
                lastRow = parsingTable.getElements().get(stateIndex);

                // We take a copy of the entry from the table and work on it
                ParsingTableRow entry = parsingTable.getElements().get(stateIndex);

                if (entry.getAction().equals(StateActionType.SHIFT)) {
                    // If the action is shift, we pop from the input stack
                    // We look at the last added state from the working stack
                    // Look into the parsing table at that state, and find out
                    // From it through what state, we can obtain the symbol popped from the input stack
                    String symbol = inputStack.pop();
                    Pair<String, Integer> state = entry.getShiftActions().stream().filter(it -> it.getFirstElement().equals(symbol)).findAny().orElse(null);

                    if (state != null) {
                        stateIndex = state.getSecondElement();
                        lastSymbol = symbol;
                        workingStack.push(new Pair<>(lastSymbol, stateIndex));
                    } else {
                        throw new NullPointerException("Action is SHIFT but there are no matching states");
                    }
                } else if (entry.getAction().equals(StateActionType.REDUCE)) {

                    List<String> reduceContent = new ArrayList<>(entry.getReductionSymbols());

                    while (reduceContent.contains(workingStack.peek().getFirstElement()) && !workingStack.isEmpty()) {
                        reduceContent.remove(workingStack.peek().getFirstElement());
                        workingStack.pop();
                    }

                    // We look into the row of the last state from the working stack
                    // We look through the shift values and look for the one that corresponds to the reduceNonTerminal
                    // Basically, we look through which state, from the current one, we can obtain that non-terminal
                    Pair<String, Integer> state = parsingTable.getElements().get(workingStack.peek().getSecondElement()).getShiftActions().stream()
                            .filter(it -> it.getFirstElement().equals(entry.getReductionNonTerminal())).findAny().orElse(null);

                    assert state != null;
                    stateIndex = state.getSecondElement();
                    lastSymbol = entry.getReductionNonTerminal();
                    workingStack.push(new Pair<>(lastSymbol, stateIndex));

                    outputStack.push(entry.getStringForReductionProd());

                    // We "form" the production used for reduction and look for its production number
                    var index = new Pair<>(entry.getReductionNonTerminal(), entry.getReductionSymbols());
                    int productionNumber = this.getProductions().indexOf(index);

                    outputNumberStack.push(productionNumber);
                } else {
                    if (entry.getAction().equals(StateActionType.ACCEPT)) {
                        List<String> output = new ArrayList<>(outputStack);
                        Collections.reverse(output);
                        List<Integer> numberOutput = new ArrayList<>(outputNumberStack);
                        Collections.reverse(numberOutput);

                        System.out.println("ACCEPTED");
                        writeToFile(filePath, "ACCEPTED");
                        System.out.println("Production strings: " + output);
                        writeToFile(filePath, "Production strings: " + output);
                        System.out.println("Production number: " + numberOutput);
                        writeToFile(filePath, "Production number: " + numberOutput);

                        ParserOutput outputTree = new ParserOutput(grammar);
                        outputTree.generateTree(numberOutput);
                        System.out.println("The output tree: ");
                        writeToFile(filePath, "The output tree: ");
                        outputTree.printTree(outputTree.getRoot(), filePath);


                        sem = false;
                    } else {
                        // Add debug print statements here to identify the issue
                        System.out.println("Unexpected state action: " + entry.getAction());
                        writeToFile(filePath, "Unexpected state action: " + entry.getAction());
                        sem = false; // Terminate the loop due to unexpected state action
                    }

                }
            } while (sem);
        } catch (NullPointerException ex) {
            // Print information about the error
            System.out.println("ERROR at state " + stateIndex + " - before symbol " + onErrorSymbol);
            System.out.println("LastRow: " + lastRow);
            ex.printStackTrace(); // Print the stack trace for detailed error location

            // Write error information to file
            writeToFile(filePath, "ERROR at state " + stateIndex + " - before symbol " + onErrorSymbol);
            writeToFile(filePath, lastRow.toString());
            ex.printStackTrace(new PrintWriter(new FileWriter(filePath, true))); // Write stack trace to file
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeToFile(String file, String line) throws IOException {
        FileWriter fw = new FileWriter(file, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(line);
        bw.newLine();
        bw.close();
    }
}