package org.example;

import lombok.Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@Data
public class FA {
    // separator used for splitting strings
    private final String separator = " ";

    // flag indicating if the FA is deterministic
    private boolean isDeterministic;

    // initial state of the FA
    private String initialState;

    // all the states of the FA
    private List<String> states;

    // all the symbols in the alphabet
    private List<String> alphabet;

    // the final states of the FA
    private List<String> finalStates;

    // transitions between states
    private final Map<Pair<String, String>, Set<String>> transitions;

    public FA(String filePath) {
        this.transitions = new HashMap<>();
        this.readFromFile(filePath);
    }

    /**
     * Read the FA from the file
     *
     * @param filePath - path to the file in which the FA is stored
     */
    private void readFromFile(String filePath) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            // initialize FA details by reading from the file
            initializeAutomatonDetails(scanner);
            // process the transitions from the file
            processTransitions(scanner);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // check if the FA is deterministic
        this.isDeterministic = checkIfDeterministic();
    }

    /**
     * Initialize FA details (states, initial state, alphabet, final states)
     *
     * @param scanner - Scanner obj. used to read FA
     */
    private void initializeAutomatonDetails(Scanner scanner) {
        // the first line contains the states (make a list of these states)
        this.states = readLineAsList(scanner);
        // the second line contains the initial state of the FA
        this.initialState = scanner.nextLine();
        // the third line contains the alphabet (store its elements in a list)
        this.alphabet = readLineAsList(scanner);
        // the fourth line contains the final states (store them in a list)
        this.finalStates = readLineAsList(scanner);
    }

    /**
     * Helper method to read a line from the scanner and split it into a list using the " " separator
     *
     * @param scanner - the scanner containing the FA
     * @return - list of elements from the same line in the FA
     */
    private List<String> readLineAsList(Scanner scanner) {
        return new ArrayList<>(List.of(scanner.nextLine().split(this.separator)));
    }

    /**
     * Method to process transitions from the scanner
     *
     * @param scanner - the scanner containing the FA
     */
    private void processTransitions(Scanner scanner) {
        // iterate through each line of the scanner
        while (scanner.hasNextLine()) {
            // read a transition
            String transitionLine = scanner.nextLine();
            // the components of the transition are separated by " "
            String[] transitionComponents = transitionLine.split(" ");
            // process separate transition components
            processTransitionComponents(transitionComponents);
        }
    }

    /**
     * Method to process individual transition components and populate the transitions map
     *
     * @param transitionComponents - a transition (a line from the FA file)
     */
    private void processTransitionComponents(String[] transitionComponents) {
        // check if the transition is valid
        if (isValidTransition(transitionComponents)) {
            // extract individual components of the transaction
            String fromState = transitionComponents[0];
            String symbol = transitionComponents[1];

            String[] toStates = Arrays.copyOfRange(transitionComponents, 2, transitionComponents.length);

            // create separate transitions for each destination state
            for (String toState : toStates) {
                Pair<String, String> transitionState = new Pair<>(fromState, symbol);
                transitions.computeIfAbsent(transitionState, k -> new HashSet<>());
                transitions.get(transitionState).add(toState);
            }
        }
    }

    /**
     * Method to check if a transition is valid
     *
     * @param transitionComponents - list of strings containing the separate elements of the transition
     * @return - TRUE if the transition is valid, FALSE otherwise
     */
    private boolean isValidTransition(String[] transitionComponents) {
        return states.contains(transitionComponents[0]) &&
                states.contains(transitionComponents[2]) &&
                alphabet.contains(transitionComponents[1]);
    }

    /**
     * Method to check if the FA is deterministic by examining its transitions
     *
     * @return - TRUE if it is deterministic, FALSE otherwise
     */
    public boolean checkIfDeterministic() {
        return this.transitions
                .values()
                .stream()
                .allMatch(list -> list.size() <= 1);
    }

    /**
     * Method to generate a string representation of the FA's transitions
     *
     * @return - formatted string of FA transitions
     */
    public String writeTransitions() {
        StringBuilder builder = new StringBuilder();

        // Create a map to group transitions by state and symbol
        Map<Pair<String, String>, List<String>> groupedTransitions = new HashMap<>();

        // Iterate through each transition and group them by (fromState, symbol) pair
        transitions.forEach((transition, toStates) -> {
            String fromState = transition.getFirst();
            String symbol = transition.getSecond();

            // Create a pair representing the transition
            Pair<String, String> transitionPair = new Pair<>(fromState, symbol);

            // Create or get the list of transitions for this pair
            groupedTransitions.computeIfAbsent(transitionPair, k -> new ArrayList<>());

            // Add each destination state for this transition pair
            for (String toState : toStates) {
                groupedTransitions.get(transitionPair).add(toState);
            }
        });

        // Iterate through the grouped transitions and construct the formatted string
        groupedTransitions.forEach((transition, toStates) -> {
            String fromState = transition.getFirst();
            String symbol = transition.getSecond();

            // Handle each destination state separately
            for (String toState : toStates) {
                builder.append("(")
                        .append(fromState)
                        .append(", ")
                        .append(symbol)
                        .append(") -> ")
                        .append(toState)
                        .append("\n");
            }
        });

        return builder.toString();
    }



    /**
     * Determines whether a given sequence is accepted by the FA by transitioning from the initial state through
     * defined transitions to reach a final state.
     *
     * @param sequence - the sequence provided by the user
     * @return - TRUE if the sequence is valid, FALSE otherwise
     */
    public boolean checkSequence(String sequence) {
        // if the sequence is empty, check if the initial state is a final state
        if (sequence.isEmpty()) {
            return finalStates.contains(initialState);
        }

        // set of possible current states for sequence processing
        Set<String> states = new HashSet<>();
        states.add(initialState);

        // process each symbol in the sequence to determine the possible final states
        for (char symbol : sequence.toCharArray()) {
            Set<String> nextStates = new HashSet<>();

            for (String state : states) {
                Pair<String, String> key = new Pair<>(state, String.valueOf(symbol));
                if (transitions.containsKey(key)) {
                    nextStates.addAll(transitions.get(key));
                }
            }

            if (nextStates.isEmpty()) {
                return false; // no transitions available for this symbol from current states
            }

            states = nextStates;
        }

        // check if any of the final states are in the possible final states set
        for (String state : states) {
            if (finalStates.contains(state)) {
                return true; // at least one final state reached
            }
        }

        return false; // none of the final states reached
    }
}
