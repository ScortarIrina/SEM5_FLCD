package org.example;

import lombok.Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Data
public class Grammar {
    private Set<String> terminals; // Set of terminal symbols in the grammar
    private Set<String> nonTerminals; // Set of non-terminal symbols in the grammar
    private Map<List<String>, List<List<String>>> productions; // Map representing the grammar productions
    private boolean isCFG; // Flag indicating if the grammar is in context free grammar(CFG)
    private boolean isGrammarEnriched; // Flag indicating if the grammar is enriched
    private String start; // The start symbol of the grammar
    public static String firstState = "S0"; // The state from which an enriched grammar begins
    private static final Logger LOGGER = Logger.getLogger(Grammar.class.getName());

    public boolean isCFG() {
        return isCFG;
    }

    public boolean isGrammarEnriched() {
        return isGrammarEnriched;
    }

    public Grammar(Set<String> nonTerminals, Set<String> terminals, String startingSymbol, Map<List<String>, List<List<String>>> productions) {
        this.nonTerminals = nonTerminals;
        this.terminals = terminals;
        this.start = startingSymbol;
        this.productions = productions;
        this.isGrammarEnriched = true;
    }

    public Grammar(String filePath) {
        this.load(filePath);
    } // Using a file to get the grammar

    /**
     * Reading a grammar from a file (for which the path to the file is specified)
     * Everything will be classified in terminals, non-terminals, etc
     **/
    private void load(String filePath) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            this.nonTerminals = new LinkedHashSet<>(List.of(scanner.nextLine().split(" ")));
            this.terminals = new LinkedHashSet<>(List.of(scanner.nextLine().split(" ")));

            this.start = scanner.nextLine(); // The start symbol of the grammar

            this.productions = new HashMap<>();
            while (scanner.hasNextLine()) {
                this.transformAndStoreProduction(scanner.nextLine());
            }

            this.isCFG = this.isGrammarCFG(); // Check if the grammar is context free
            this.isGrammarEnriched = false;
        } catch (FileNotFoundException e) {
            LOGGER.severe("Error while reading the file!");
            LOGGER.severe(e.getMessage());
        }
    }


    // Process a production string and store the parsed information into a list
    private void transformAndStoreProduction(String production) {
        // Split the production into left-hand side and right-hand side using the separator ("::=")
        String[] parts = production.split("::=");

        // Split the lhs into symbols using the concatenation symbol
        List<String> leftHandSide = List.of(parts[0].split(" "));

        // Split the rhs into individual productions using the separator ("|")
        String[] rightHandSideProductions = parts[1].split("\\|");

        // Initialize the map entry if it does not exist
        this.productions.putIfAbsent(leftHandSide, new ArrayList<>());

        // Process each production from the rhs and format them for storage in the map
        for (String rightHandSideProduction : rightHandSideProductions) {
            // Format each rhs production by splitting and collecting into a list
            this.productions.get(leftHandSide).add(
                    Arrays.stream(rightHandSideProduction.split(" "))
                            .collect(Collectors.toList()));
        }
    }


    // Method to check if our grammar is context-free
    private boolean isGrammarCFG() {
        // Check if the starting symbol is part of the non-terminals
        if (!this.nonTerminals.contains(this.start)) { // If not, is not CFG
            return false;
        }

        // Checking if on the left-hand side, each production has only 1 non-terminal
        for (List<String> lhs : this.productions.keySet()) {
            // LHS needs to have only 1 element
            // Also, we check if that element is part of the non-terminals
            if (lhs.size() != 1 || !this.nonTerminals.contains(lhs.get(0))) {
                return false;
            }

            for (List<String> possibleNextMoves : this.productions.get(lhs)) {
                for (String move : possibleNextMoves) { // The moves are the productions of the lhs
                    // Checking if the productions of the lhs are either a terminal, non-terminal or epsilon
                    if (!this.nonTerminals.contains(move) && !this.terminals.contains(move) && !move.equals("EPSILON")) {
                        return false;
                    }
                }
            }
        }

        return true; // The grammar is CFG
    }


    // Returns the productions of a specified non-terminal, given as parameter
    public List<List<String>> getAllProductionsForNonTerminal(String nonTerminal) {
        return getProductions().get(List.of(nonTerminal));
    }


    // Method to transform a grammar into an enriched form suitable for the LR(0) parsing algorithm
    public Grammar makeGrammarEnriched() throws Exception {
        if (isGrammarEnriched) {
            throw new Exception("The Grammar is already enriched!");
        }

        // Create a new form of the grammar to be enriched
        Grammar enrichedGrammar = new Grammar(nonTerminals, terminals, firstState, productions);

        // Adding another initial state 'S0' to the list of non-terminals, so then S0 will produce our firstState
        // In other words, S0 produces the original starting symbol of the grammar
        enrichedGrammar.nonTerminals.add(firstState);
        // Adding a new entry to the map by initializing an empty list as the rhs, if there is no existing entry for firstState
        enrichedGrammar.productions.putIfAbsent(List.of(firstState), new ArrayList<>());
        // Marking that the new initial state S0 produces the same as the original grammar's starting symbol
        enrichedGrammar.productions.get(List.of(firstState)).add(List.of(start));

        return enrichedGrammar;
    }


    /** Method that extract individual productions from the grammar, representing each one as a pair (lhs, rhs)
     *  where lhs is a non-terminal from the left-hand side, and rhs is a list of symbols from the right-hand side.
     */
    public List<Pair<String, List<String>>> getAllIndividualProductions() {
        // List to store the pairs representing each production individually
        List<Pair<String, List<String>>> result = new ArrayList<>();

        // Iterate through each production in the grammar
        this.productions.forEach(
                (lhs, rhsList) -> {
                    // For each right-hand side in the production, create a pair (lhs, rhs) and add it to the result list
                    rhsList.forEach(
                            (rhs) -> result.add(new Pair<>(lhs.get(0), rhs))
                    );
                }
        );

        // Return the list of pairs representing each individual production
        return result;
    }
}