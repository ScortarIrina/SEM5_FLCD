package org.example;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CanonicalCollection {
    private List<State> states; // The canonical collection contains multiple states

    public CanonicalCollection(){
        this.states = new ArrayList<>();
    }

    // Adds a new state in the collection
    public void addState(State newState){
        int index = states.size();
        this.states.add(index, newState); // Adds the new state at the position (adding at the end),
        // moving to the right any states already there
    }
}
