package org.example;

import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
public class CanonicalCollection {
    private List<State> states; // The canonical collection contains multiple states
    private Map<Pair<Integer, String>, Integer> adjacencyList;

    public CanonicalCollection(){
        this.states = new ArrayList<>();
        this.adjacencyList = new LinkedHashMap<>();
    }

    // Adds a new state in the collection
    public void addState(State newState){
        this.states.add(states.size(), newState);
    }

    public void connectStates(Integer indexFirstState, String symbol, Integer indexSecondState){
        this.adjacencyList.put(new Pair<>(indexFirstState, symbol), indexSecondState);
    }
}
