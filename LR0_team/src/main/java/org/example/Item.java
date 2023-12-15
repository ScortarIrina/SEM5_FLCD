package org.example;

import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * An item has the form [A->alpha.beta]
 * Here the idea is the following one, the element preceded by the dot is
 * considered to be the element from the dot position (that's how we represent the dot
 * without actually representing it physically)
 * We have a string for the left hand side, a list of strings for the right hand side
 * and the position for the dot
 */
@Data
public class Item {
    private String leftHandSide; // the part of the item before the dot ( 'a' in [A->a.b] )
    private List<String> rightHandSide; // the part of the item before the dot ( 'b' in [A->a.b] )
    private Integer dotLocation; // the position of the dot

    public Item(String lhs, List<String> rhs, Integer dotPosition) {
        this.leftHandSide = lhs;
        this.rightHandSide = rhs;
        this.dotLocation = dotPosition;
    }

    @Override
    public String toString() {
        String left = String.join("", leftHandSide);
        String right1 = String.join("", rightHandSide.subList(0, dotLocation));
        String right2 = String.join("", rightHandSide.subList(dotLocation, rightHandSide.size()));

        return left + "->" + right1 + "." + right2;
    }


    @Override
    public int hashCode() {
        return Objects.hash(leftHandSide, rightHandSide, dotLocation);
    }

    @Override
    public boolean equals(Object obj) {
        // Check if the objects are the same instance
        if (this == obj) {
            return true;
        }

        // Check if the other object is an instance of Item
        if (obj instanceof Item otherItem) {

            // Compare the fields for equality using Objects.equals
            return Objects.equals(leftHandSide, otherItem.leftHandSide)
                    && Objects.equals(rightHandSide, otherItem.rightHandSide)
                    && Objects.equals(dotLocation, otherItem.dotLocation);
        }

        // If the other object is not an instance of Item, they are not equal
        return false;
    }


}
