package org.example;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;

public class HashTable<T> {
    private final ArrayList<ArrayList<T>> elements;
    private final int size;

    public HashTable(int size) {
        this.size = size;
        this.elements = new ArrayList<>();
        for (int i = 0; i < size; i++)
            this.elements.add(new ArrayList<>());
    }

    // hash function for integers
    private int hash(int key) {
        return key % size;
    }

    // hash function for strings
    private int hash(String key) {
        int sum = 0;
        for (int i = 0; i < key.length(); i++) {
            sum += key.charAt(i);
        }
        return sum % size;
    }

    // generic hashing function
    private int getHashValue(T key) {
        int hashValue = -1;
        if (key instanceof Integer) {
            hashValue = hash((int) key);        // hashing an int
        } else if (key instanceof String) {
            hashValue = hash((String) key);     // hashing a string
        }
        return hashValue;
    }

    public Pair<Integer, Integer> add(T key) throws Exception {
        // compute the hash value for the input key
        int hashValue = getHashValue(key);

        // check if key is not already in the table
        if (!elements.get(hashValue).contains(key)) {
            // add key to the appropriate inner ArrayList
            elements.get(hashValue).add(key);

            // return a Pair containing the hash value and the position of the key.
            return new ImmutablePair<>(hashValue, elements.get(hashValue).indexOf(key));
        }
        throw new Exception("\n!!! EXCEPTION: Key " + key + " is already in the table!\n");
    }

    public boolean contains(T key) {
        int hashValue = getHashValue(key);
        return elements.get(hashValue).contains(key);
    }

    // returns the position of the given key in the table
    public Pair<Integer, Integer> getPosition(T key) {
        if (this.contains(key)) {
            int hashValue = getHashValue(key);
            return new ImmutablePair<>(hashValue, elements.get(hashValue).indexOf(key));
        }
        return new ImmutablePair<>(-1, -1);
    }

    @Override
    public String toString() {
        return elements.toString();
    }
}
