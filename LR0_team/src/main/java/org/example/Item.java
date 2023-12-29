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

    public boolean dotIsLast(){
        return this.dotLocation == this.rightHandSide.size();
    }

    @Override
    public String toString() {
        List<String> rightHandSide1 = this.rightHandSide.subList(0, dotLocation);

        String stringRightHandSide1 = String.join("", rightHandSide1);

        List<String> rightHandSide2 = this.rightHandSide.subList(dotLocation, this.rightHandSide.size());

        String stringLeftHandSide2 = String.join("", rightHandSide2);

        return leftHandSide + "->" + stringRightHandSide1 + "." + stringLeftHandSide2;
    }


    @Override
    public int hashCode() {
        return Objects.hash(leftHandSide, rightHandSide, dotLocation);
    }

    @Override
    public boolean equals(Object item) {
        return item instanceof Item && Objects.equals(((Item)item).leftHandSide, this.leftHandSide) &&
                ((Item)item).rightHandSide == rightHandSide && Objects.equals(((Item)item).dotLocation, this.dotLocation);
    }
}
