package org.example;

import lombok.Data;

@Data
public class ParsingTreeNode {
    private Integer index; // this is the number of the node
    private String symbol; // the content of the node
    private ParsingTreeNode parent; // parent parsing tree node that contains this node as a child
    private ParsingTreeNode sibling; // next node at the same level
    private ParsingTreeNode child; // first child at the next level
    private Integer depth; // indicates where in the hierarchy of the tree we are now, or what level of the tree

    public ParsingTreeNode(String newData) {
        this.symbol = newData;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(index).append(": \n");
        stringBuilder.append("-- symbol = ").append(symbol).append('\n');

        if (parent != null) {
            stringBuilder.append("-- parent = ").append(parent.getIndex()).append('\n');
        } else {
            stringBuilder.append("-- parent = -1\n");
        }

        if (sibling != null) {
            stringBuilder.append("-- sibling = ").append(sibling.getIndex()).append('\n');
        } else {
            stringBuilder.append("-- sibling = -1\n");
        }

        if (child != null) {
            stringBuilder.append("-- child = ").append(child.getIndex()).append('\n');
        } else {
            stringBuilder.append("-- child = -1\n");
        }

        stringBuilder.append("-- depth = ").append(depth).append('\n');


        return stringBuilder.toString();
    }
}
