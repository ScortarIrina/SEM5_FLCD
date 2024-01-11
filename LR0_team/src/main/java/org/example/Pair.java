package org.example;

import lombok.Data;

@Data
public class Pair<Object1, Object2> {
    private final Object1 firstElement; // first element of the pair
    private final Object2 secondElement; // second element of the pair

    @Override
    public String toString() {
        return "Pair{" +
                "firstElement=" + firstElement +
                ", secondElement=" + secondElement +
                '}';
    }
}