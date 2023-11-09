package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Pair<First, Second> {
    private final First first;
    private final Second second;

    @Override
    public String toString() {
        return "Pair {" +
                "first = " + first +
                ", second = " + second +
                '}';
    }
}
