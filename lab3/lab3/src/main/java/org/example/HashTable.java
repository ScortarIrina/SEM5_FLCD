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
     * This method searches for the position of an element in the hash table.
     *
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
     * This method hashes the given element using the ASCII representation of its characters.
     *
     * @param key - the element for which we compute the hash
     * @return - the hash of the element
     */
    private Integer hash(String key) {
        int sumAsciiOfChars = 0;
        char[] keyCharacters = key.toCharArray();
        for (char c : keyCharacters) {
            sumAsciiOfChars += c;
        }
        return sumAsciiOfChars % size;
    }

    /**
     * This method checks whether the hashmap contains a specific element
     *
     * @param elem - the element we are looking for in the hashmap
     * @return - TRUE if the element is in the hashmap
     *           FALSE otherwise
     */
    public boolean containsTerm(String elem) {
        return this.findPositionOfTerm(elem) != null;
    }

    /**
     * This method adds a new element in the hashmap
     *
     * @param elem - the element we want to add in the hashmap
     * @return - TRUE if the element was added successfully in the hashmap
     *           FALSE if the element was already in the hashmap
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
            if (!this.table.get(i).isEmpty()) {
                computedString.append(i);
                computedString.append(" - ");
                computedString.append(this.table.get(i));
                computedString.append("\n");
            }
        }
        return computedString.toString();
    }

}