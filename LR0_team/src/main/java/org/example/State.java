package org.example;

import lombok.Data;

import java.util.*;

@Data
public class State {
    private Set<Item> items; // The state contains multiple LR items -> all items corresponding to same live prefix
    private StateActionType stateActionType;

    public State(Set<Item> items) {
        this.items = items;
        this.setActionForState();
    }

    private void setActionForState() {
        if (items.size() == 1 && ((Item) items.toArray()[0]).getRightHandSide().size() == ((Item) items.toArray()[0]).getDotLocation() && Objects.equals(((Item) this.items.toArray()[0]).getLeftHandSide(), Grammar.firstState)) {
            this.stateActionType = StateActionType.ACCEPT;
        } else if (items.size() == 1 && ((Item) items.toArray()[0]).getRightHandSide().size() == ((Item) items.toArray()[0]).getDotLocation()) {
            this.stateActionType = StateActionType.REDUCE;
        } else if (!items.isEmpty() && this.items.stream().allMatch(i -> i.getRightHandSide().size() > i.getDotLocation())) {
            this.stateActionType = StateActionType.SHIFT;
        } else if (items.size() > 1 && this.items.stream().allMatch(i -> i.getRightHandSide().size() == i.getDotLocation())) {
            this.stateActionType = StateActionType.REDUCE_REDUCE_CONFLICT;
        } else {
            this.stateActionType = StateActionType.SHIFT_REDUCE_CONFLICT;
        }
    }

    // Retrieving the symbols after the dot in LR(0) items
    public List<String> getPartAfterTheDot() {
        Set<String> symbols = new LinkedHashSet<>();

        for(Item i: items){
            if(i.getDotLocation() < i.getRightHandSide().size())
                symbols.add(i.getRightHandSide().get(i.getDotLocation()));
        }

        return new ArrayList<>(symbols);
    }

    @Override
    public int hashCode(){
        return Objects.hash(items);
    }

    @Override
    public boolean equals(Object item){
        if(item instanceof  State){
            return ((State) item).getItems().equals(this.getItems());
        }

        return false;
    }

    @Override
    public String toString(){
        return stateActionType + " - " + items;
    }
}
