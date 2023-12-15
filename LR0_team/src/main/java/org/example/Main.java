package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void printMenu() {
        System.out.println("\n\n~~~~~~~~~~~~~~~~ MENU ~~~~~~~~~~~~~~~\n");
        System.out.println("0. Exit");
        System.out.println("1. To print non-terminals, press 1.");
        System.out.println("2. To print terminals, press 2.");
        System.out.println("3. To print the starting symbol, press 3.");
        System.out.println("4. To print all productions, press 4.");
        System.out.println("5. To print all productions for a non-terminal, press 5.");
        System.out.println("6. To check if the grammar is a context-free grammar (CFG), press 6.");
        System.out.println("7. To run LR0, press 7.");
        System.out.println("8. To run all tests for the grammar and LR0, press 8.");
    }


    public static void runGrammarMenu() throws Exception {
        Grammar grammar = new Grammar("src/main/java/org/example/IO/G1.txt");
        boolean isMenuRunning = true;

        while (isMenuRunning) {
            printMenu();
            Scanner sc = new Scanner(System.in);
            System.out.print("\nEnter your option: ");
            int option = sc.nextInt();

            if (option == 0) {
                isMenuRunning = false;
            } else if (option == 1) {
                System.out.println("\n\nNon-terminals -> " + grammar.getNonTerminals());
            } else if (option == 2) {
                System.out.println("\n\nTerminals -> " + grammar.getTerminals());
            } else if (option == 3) {
                System.out.println("\n\nStarting symbol -> " + grammar.getStart());
            } else if (option == 4) {
                System.out.println("\n\nAll productions: ");
                grammar.getProductions().forEach((lhs, rhs) -> System.out.println(lhs + " -> " + rhs));
            } else if (option == 5) {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Introduce a non-terminal: ");
                String nonTerminal = scanner.nextLine();
                System.out.println("\n\nProductions of the non-terminal: " + nonTerminal);
                List<String> stringList = new ArrayList<>();
                stringList.add(nonTerminal);
                try {
                    grammar.getProductions().get(stringList).forEach((rhs) -> System.out.println(stringList + " -> " + rhs));
                } catch (NullPointerException e) {
                    System.out.println("Error! This non-terminal doesn't exist!");
                }
            } else if (option == 6) {
                boolean result = grammar.isCFG();
                if (result) {
                    System.out.println("The grammar is CFG (context-free grammar).");
                } else {
                    System.out.println("The grammar is not CFG.");
                }
            } else if (option == 7) {
                Grammar g = new Grammar("src/main/java/org/example/IO/G1.txt");
                LR0 lr0 = new LR0(g);
                List<State> states = lr0.getCanonicalCollectionForGrammar().getStates();

                System.out.println("LR(0) Canonical Collection States:");

                int length = states.size();
                for (int i = 0; i < length; i++) {
                    System.out.println("State " + i + ":");
                    State state = states.get(i);
                    System.out.println("\t" + state.getItems());
                    System.out.println();
                }
            } else if (option == 8) {
                Test test = new Test();
                test.testClosure();
                test.testGoto();
                test.testCanonicalCollections();
            } else {
                System.out.println("Error!!! That is not a valid option!!! Please try again!");
            }
        }
    }


    public static void main(String[] args) throws Exception {
        runGrammarMenu();
    }
}