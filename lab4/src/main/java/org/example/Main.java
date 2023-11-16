package org.example;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public class Main {
    private static void printToFile(String filePath, Object object) {
        try (PrintStream printStream = new PrintStream(filePath)) {
            printStream.println(object);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void run(String filePath) {
        MyScanner scanner = new MyScanner(filePath);
        scanner.scan();
        printToFile(filePath.replace(".txt", "ST.out"), scanner.getSymbolTable());
        printToFile(filePath.replace(".txt", "PIF.out"), scanner.getPif());
    }

    private static void printMenu() {
        System.out.println("0. Exit");
        System.out.println("1. States");
        System.out.println("2. Alphabet");
        System.out.println("3. Final states");
        System.out.println("4. Transitions");
        System.out.println("5. Initial state");
        System.out.println("6. Is it deterministic?");
        System.out.println("7. Check if sequence is accepted by DFA");
    }

    private static void optionsForDFA() {

        FA finiteAutomaton = new FA("/Users/irinascortar/Desktop/JavaProjects/SEM5_FLCD/lab4/src/main/java/org/example/IO/FA.txt");

        System.out.println("FA read from file.\n");
        printMenu();
        System.out.println("\nYour option: ");

        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();

        while (option != 0) {

            switch (option) {
                case 1:
                    System.out.println("States: ");
                    System.out.println(finiteAutomaton.getStates());
                    System.out.println();
                    break;

                case 2:
                    System.out.println("Alphabet: ");
                    System.out.println(finiteAutomaton.getAlphabet());
                    System.out.println();
                    break;

                case 3:
                    System.out.println("Final states: ");
                    System.out.println(finiteAutomaton.getFinalStates());
                    System.out.println();
                    break;

                case 4:
                    System.out.println(finiteAutomaton.writeTransitions());
                    break;

                case 5:
                    System.out.println("Initial state: ");
                    System.out.println(finiteAutomaton.getInitialState());
                    System.out.println();
                    break;

                case 6:
                    System.out.println("Is it deterministic?");
                    System.out.println(finiteAutomaton.checkIfDeterministic());
                    break;

                case 7: {
                    System.out.println("Your sequence: ");
                    Scanner scanner2 = new Scanner(System.in);
                    String sequence = scanner2.nextLine();

                    if (finiteAutomaton.checkSequence(sequence))
                        System.out.println("Sequence is valid");
                    else
                        System.out.println("Invalid sequence");
                }
                break;

                default:
                    System.out.println("Invalid command!");
                    break;

            }
            System.out.println();
            printMenu();
            System.out.println("Your option: ");
            option = scanner.nextInt();
        }
    }

    public static void runScanner() {
        run("/Users/irinascortar/Desktop/JavaProjects/SEM5_FLCD/lab4/src/main/java/org/example/IO/p1/p1.txt");
        run("/Users/irinascortar/Desktop/JavaProjects/SEM5_FLCD/lab4/src/main/java/org/example/IO/p2/p2.txt");
        run("/Users/irinascortar/Desktop/JavaProjects/SEM5_FLCD/lab4/src/main/java/org/example/IO/p3/p3.txt");
        run("/Users/irinascortar/Desktop/JavaProjects/SEM5_FLCD/lab4/src/main/java/org/example/IO/p1err/p1err.txt");
    }

    public static void main(String[] args) {
        System.out.println("\n0. Exit");
        System.out.println("1. Finite automaton");
        System.out.println("2. Scanner");
        System.out.println("\nYour option: ");

        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();

        switch (option) {
            case 0:
                break;
            case 1:
                optionsForDFA();
                break;
            case 2:
                runScanner();
                break;

            default:
                System.out.println("Invalid command!");
                break;

        }

    }
}