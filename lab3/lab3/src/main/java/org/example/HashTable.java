package org.example;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class HashTable {
    private Integer size;
    private ArrayList<ArrayList<String>> table;


    public HashTable(Integer size) {
        this.size = size;
        this.table = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            this.table.add(new ArrayList<>());
        }
    }

    /**
     * With this method we look for the position of an element and return it
     * @param elem - the element for which we want to find the position
     * @return - the position of the element
     */
    public Pair<Integer, Integer> findPositionOfTerm(String elem) {
        int pos = hash(elem);

        if (!table.get(pos).isEmpty()) {
            ArrayList<String> elems = this.table.get(pos);
            for (int i = 0; i < elems.size(); i++) {
                if (elems.get(i).equals(elem)) {
                    return new Pair<>(pos, i);
                }
            }
        }

        return null;
    }

    /**
     * Function used for hashing an element
     * @param key - the element for which we compute the hash
     * @return - the hash of the element
     */
    private Integer hash(String key) {
        int sumCharacters = 0;
        char[] keyCharacters = key.toCharArray();
        for (char c : keyCharacters) {
            sumCharacters += c;
        }
        return sumCharacters % size;
    }

    /**
     * This method looks whether the hashmap contains a specific element
     * @param elem - the element we are looking for in the hashmap
     * @return - returns true if the element is in the hashmap and false otherwise
     */
    public boolean containsTerm(String elem) {
        return this.findPositionOfTerm(elem) != null;
    }

    /**
     * This method adds a new element in the hashmap
     * @param elem - the element we want to add in the hashmap
     * @return - return true if the element was added successfully in the hashmap and false if the element was already
     * in the hashmap
     */
    public boolean add(String elem) {
        if (containsTerm(elem)) {
            return false;
        }

        Integer pos = hash(elem);

        ArrayList<String> elems = this.table.get(pos);
        elems.add(elem);

        return true;
    }

    @Override
    public String toString() {
        StringBuilder computedString = new StringBuilder();
        for (int i = 0; i < this.table.size(); i++) {
            if (this.table.get(i).size() > 0) {
                computedString.append(i);
                computedString.append(" - ");
                computedString.append(this.table.get(i));
                computedString.append("\n");
            }
        }
        return computedString.toString();
    }

}