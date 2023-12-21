package org.example;

import lombok.Data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
public class ParserOutput {
    private ParsingTreeNode root; // root of the tree
    private Grammar grammar; // associated grammar of the parsing tree
    private int currentIndex = 1; // current index used for creating unique indices for parsing tree nodes
    private int inputPosition = 1; // index of the parsing tree node in the input sequence during tree generation
    private int maxDepth = 0; // maximum depth of the parsing tree
    private List<ParsingTreeNode> treeNodeList; // parsing tree nodes in the tree

    public ParserOutput(Grammar grammar){
        this.grammar = grammar;
    }

    // Generate a parsing tree, based on a sequence
    // returns the root of the created tree
    public ParsingTreeNode generateTree(List<Integer> inputSequence) {
        // Get the index of the first production from the input sequence.
        int index = inputSequence.get(0);

        // Retrieve the production string associated with the production index from the grammar.
        Pair<String, List<String>> productionString = this.grammar.getAllIndividualProductions().get(index);

        // Create a new parsing tree node (root) with the non-terminal from the production string.
        this.root = new ParsingTreeNode(productionString.getFirstElement());

        // Set index and depth for the root node.
        this.root.setIndex(0);
        this.root.setDepth(0);

        // Recursively build the parsing tree starting from the root node.
        // The child of the root is constructed based on the elements of the production string.
        this.root.setChild(buildRecursive(1, this.root, productionString.getSecondElement(), inputSequence));

        // Return the root of the generated parsing tree.
        return this.root;
    }


    /*
      Computes the order in which nodes should appear in the parsing tree and creates a list
      Starting from the given node, it traverses the tree in a depth-first manner
      The nodes are added to the list as they are encountered, ensuring the desired order
     */
    public void traverseAndBuildParsingTreeList(ParsingTreeNode root) {
        // Check if the provided root is null
        if (root == null) {
            // If the root is null, there is nothing to add to the list
            return;
        }

        // Use a while loop to traverse the tree in a depth-first manner
        while (root != null) {
            // Add the current root to the list
            this.treeNodeList.add(root);

            // If the current root has a child, recursively call createList for the child
            if (root.getChild() != null) {
                traverseAndBuildParsingTreeList(root.getChild());
            }

            // Move to the next root at the same level (right sibling)
            root = root.getSibling();
        }
    }


    // Prints the parsing tree nodes in the order of their depth and writes them to a specified file.
    public void printTree(ParsingTreeNode root, String filePath) throws IOException {
        // Initialize a list to store parsing tree nodes in the order of their appearance.
        this.treeNodeList = new ArrayList<>();

        // Create a list by computing the order in which nodes should appear in the parsing tree.
        traverseAndBuildParsingTreeList(root);

        // Temporary variable to store the string representation of each parsing tree node.
        String auxString;

        // Determine the maximum depth of the parsing tree.
        int length = this.maxDepth;

        // Iterate through each depth level in the parsing tree.
        for (int i = 0; i <= length; i++) {
            // Iterate through each parsing tree node in the list.
            for (ParsingTreeNode node : this.treeNodeList) {
                // Check if the node's depth matches the current depth level.
                if (node.getDepth() == i) {
                    // Print the node to the console.
                    System.out.println(node);

                    // Convert the node to its string representation.
                    auxString = node.toString();

                    // Write the string representation to the specified file.
                    writeStringToFile(filePath, auxString);
                }
            }
        }
    }

    // Write a given string to a file
    public void writeStringToFile(String file, String newString) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
        writer.write(newString); // add the string to the content of the file
        writer.newLine(); // adds a line separator at the end of the file
        writer.close();
    }


    // Method to recursively build the nodes of the parsing tree
    public ParsingTreeNode buildRecursive(int treeDepth, ParsingTreeNode parent, List<String> currentProduction, List<Integer> inputSequence){
        // Check if there are no more symbols to process or if the input position exceeds the input sequence size
        if (currentProduction.isEmpty() || this.inputPosition >= inputSequence.size() + 1) {
            return null;
        }

        // Take the current symbol
        String currentSymbol = currentProduction.get(0);

        // Determine if the current symbol is a terminal or a non-terminal
        boolean isTerminal = this.grammar.getTerminals().contains(currentSymbol);
        boolean isNonTerminal = this.grammar.getNonTerminals().contains(currentSymbol);



        // If the symbol is a terminal, create a terminal node (it can't be a parent)
        if (isTerminal) {
            ParsingTreeNode node = new ParsingTreeNode(currentSymbol);

            // Set index, level, and parent for the terminal node
            node.setIndex(this.currentIndex++);
            node.setDepth(treeDepth);
            node.setParent(parent);

            // Remove the processed symbol from the current production
            List<String> newProductionList = new ArrayList<>(currentProduction);
            newProductionList.remove(0);

            // Set the right sibling using recursion
            node.setSibling(buildRecursive(treeDepth, parent, newProductionList, inputSequence));

            return node;
        }

        // If the symbol is a non-terminal, create a non-terminal node (it can be a parent)
        else if (isNonTerminal) {
            // Get the index corresponding to the non-terminal
            int indexForNonTerminal = inputSequence.get(this.inputPosition);

            // Get all productions for that index
            Pair<String, List<String>> productionString = this.grammar.getAllIndividualProductions().get(indexForNonTerminal);

            // Create a new node in the tree
            ParsingTreeNode node = new ParsingTreeNode(currentSymbol);

            // Set index, level, and parent for the newly created node
            node.setIndex(this.currentIndex++);
            node.setDepth(treeDepth);
            node.setParent(parent);

            // Increase the depth of the tree for a new child
            int lvl = treeDepth + 1;
            if (lvl > this.maxDepth) {
                this.maxDepth = lvl;
            }

            // Increment position and call recursion to set the value of the child
            this.inputPosition++;
            node.setChild(buildRecursive(lvl, node, productionString.getSecondElement(), inputSequence));

            // Remove the processed symbol
            List<String> newProductionList = new ArrayList<>(currentProduction);
            newProductionList.remove(0);

            // Use the recursive building to set the new value of the sibling
            node.setSibling(buildRecursive(treeDepth, parent, newProductionList, inputSequence));

            return node;
        }

        // Handle the case when the current symbol is neither a terminal nor a non-terminal
        else {
            System.out.println("Error while building the tree!!");
            return null;
        }
    }
}
