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
        System.out.println("3. Initial state");
        System.out.println("4. Final states");
        System.out.println("5. Transitions");
        System.out.println("6. Is it deterministic?");
        System.out.println("7. Check if sequence is accepted by DFA");
    }

    private static void optionsForDFA() {

        FA finiteAutomaton = new FA("/Users/irinascortar/Desktop/JavaProjects/SEM5_FLCD/lab4/src/main/java/org/example/IO/FA.txt");

        printMenu();
        System.out.print("\nChoose an option (0-7): ");

        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();

        while (option != 0) {

            switch (option) {
                case 1:
                    System.out.print("The states of the FA are: ");
                    System.out.println(finiteAutomaton.getStates());
                    break;

                case 2:
                    System.out.print("The elements of the alphabet are: ");
                    System.out.println(finiteAutomaton.getAlphabet());
                    break;

                case 3:
                    System.out.print("The initial state of the FA is: ");
                    System.out.println(finiteAutomaton.getInitialState());
                    break;

                case 4:
                    System.out.print("The final states of the FAQ are: ");
                    System.out.println(finiteAutomaton.getFinalStates());
                    break;

                case 5:
                    System.out.print("The transitions of the FA are:\n");
                    System.out.println(finiteAutomaton.writeTransitions());
                    break;

                case 6:
                    System.out.print("Is it deterministic? ");
                    System.out.println(finiteAutomaton.checkIfDeterministic());
                    break;

                case 7: {
                    System.out.print("Your sequence: ");
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
            System.out.print("Choose an option (0-7): ");
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
        System.out.print("\nChoose an option (0-2): ");

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