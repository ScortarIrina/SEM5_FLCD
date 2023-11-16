package org.example;

import lombok.Data;

import java.util.ArrayList;

@Data
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
     * Search for the position of a given element
     *
     * @param elem - the element whose position we are looking for
     * @return - its position
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
     * Method used to compute the hash of an element
     *
     * @param key - the element for which we compute the hash
     * @return - its hash
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
     * CHeck if the hash table contains an element
     *
     * @param elem - the element
     * @return - TRUE if the element is in the hashmap
     * FALSE otherwise
     */
    public boolean containsTerm(String elem) {
        return this.findPositionOfTerm(elem) != null;
    }

    /**
     * Method to add an element to the hash table
     *
     * @param elem - the element to be added
     * @return - TRUE if the element was added successfully
     * FALSE if the element was already added
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
                computedString.append(" --- ");
                computedString.append(this.table.get(i));
                computedString.append("\n");
            }
        }
        return computedString.toString();
    }

}
