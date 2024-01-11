package org.example;

import org.example.Scanner.MyScanner;

import java.io.*;
import java.util.*;

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
        System.out.println("7. To run LR0 for G1.txt and parse sequence.txt, press 7.");
        System.out.println("8. To run LR0 for G2.txt, press 8.");
        System.out.println("9. To run all tests for the grammar and LR0, press 9.");
    }

    public static void printMenuParser() {
        System.out.println("\n0. Exit");
        System.out.println("1. Parse p1.txt");
        System.out.println("2. Parse p2.txt");
        System.out.println("3. Parse p3.txt\n");
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
                emptyFile("src/main/java/org/example/IO/out1.txt");

                Grammar grammar1 = new Grammar("src/main/java/org/example/IO/G1.txt");
                LR0 lrAlg = new LR0(grammar1);

                CanonicalCollection canonicalCollection = lrAlg.getCanonicalCollectionForGrammar();

                System.out.println("States");
                writeToFile("src/main/java/org/example/IO/out1.txt", "States");

                for (int i = 0; i < canonicalCollection.getStates().size(); i++) {
                    System.out.println(i + " " + canonicalCollection.getStates().get(i));
                }

                System.out.println("\nState.State transitions");
                writeToFile("src/main/java/org/example/IO/out1.txt", "\nState transitions");

                for (Map.Entry<Pair<Integer, String>, Integer> entry : canonicalCollection.getAdjacencyList().entrySet()) {
                    System.out.println(entry.getKey() + " -> " + entry.getValue());
                    writeToFile("src/main/java/org/example/IO/out1.txt", entry.getKey() + " -> " + entry.getValue());
                }

                System.out.println();

                ParsingTable parsingTable = lrAlg.getParsingTable(canonicalCollection);
                if (parsingTable.getElements().isEmpty()) {
                    System.out.println("We have conflicts in the parsing table so we can't go further with the algorithm");
                    writeToFile("src/main/java/org/example/IO/out1.txt", "We have conflicts in the parsing table so we can't go further with the algorithm");
                } else {
                    System.out.println(parsingTable);
                    writeToFile("src/main/java/org/example/IO/out1.txt", parsingTable.toString());
                }

                Stack<String> word = readSequence("src/main/java/org/example/IO/sequence.txt");

                lrAlg.parse(word, parsingTable, "src/main/java/org/example/IO/out1.txt");

                break;
            } else if (option == 8) {
                emptyFile("src/main/java/org/example/IO/out2.txt");
                Grammar grammar2 = new Grammar("src/main/java/org/example/IO/G2.txt");
                LR0 lrAlg2 = new LR0(grammar2);

                CanonicalCollection canonicalCollection2 = lrAlg2.getCanonicalCollectionForGrammar();

                System.out.println("States");
                for (int i = 0; i < canonicalCollection2.getStates().size(); i++) {
                    System.out.println(i + " " + canonicalCollection2.getStates().get(i));
                }

                System.out.println("\nState.State transitions");
                for (Map.Entry<Pair<Integer, String>, Integer> entry : canonicalCollection2.getAdjacencyList().entrySet()) {
                    System.out.println(entry.getKey() + " -> " + entry.getValue());
                }
                System.out.println();

                ParsingTable parsingTable2 = lrAlg2.getParsingTable(canonicalCollection2);
                if (parsingTable2.getElements().isEmpty()) {
                    System.out.println("We have conflicts in the parsing table so we can't go further with the algorithm");
                    writeToFile("src/main/java/org/example/IO/out2.txt", "We have conflicts in the parsing table so we can't go further with the algorithm");
                } else {
                    System.out.println(parsingTable2);
                }

                boolean stop = false;
                while (!stop) {
                    printMenuParser();
                    Scanner keyboard2 = new Scanner(System.in);
                    System.out.print("Your option: ");
                    int option2 = keyboard2.nextInt();

                    switch (option2) {
                        case 0:
                            stop = true;
                            break;
                        case 1:
                            emptyFile("src/main/java/org/example/IO/out1.txt");
                            MyScanner scanner2 = new MyScanner("src/main/java/org/example/IO/p1.txt");
                            scanner2.scan();
                            printToFile("src/main/java/org/example/IO/p1.txt".replace(".txt", "PIF.txt"), scanner2.getPif());

                            Stack<String> word2 = readFirstElemFromFile("src/main/java/org/example/IO/p1PIF.txt");

                            lrAlg2.parse(word2, parsingTable2, "src/main/java/org/example/IO/out2.txt");
                            break;

                        case 2:
                            emptyFile("src/main/java/org/example/IO/out2.txt");
                            MyScanner scanner3 = new MyScanner("src/main/java/org/example/IO/p2.txt");
                            scanner3.scan();
                            printToFile("src/main/java/org/example/IO/p2.txt".replace(".txt", "PIF.txt"), scanner3.getPif());

                            Stack<String> word3 = readFirstElemFromFile("src/main/java/org/example/IO/p2PIF.txt");

                            lrAlg2.parse(word3, parsingTable2, "src/main/java/org/example/IO/out2.txt");
                            break;

                        case 3:
                            emptyFile("src/main/java/org/example/IO/out3.txt");
                            MyScanner scanner4 = new MyScanner("src/main/java/org/example/IO/p3.txt");
                            scanner4.scan();
                            printToFile("src/main/java/org/example/IO/p3.txt".replace(".txt", "PIF.txt"), scanner4.getPif());

                            Stack<String> word4 = readFirstElemFromFile("src/main/java/org/example/IO/p3PIF.txt");

                            lrAlg2.parse(word4, parsingTable2, "src/main/java/org/example/IO/out3.txt");
                            break;
                    }
                }


                break;
            } else if (option == 9) {
                Test test = new Test();
                test.testClosure();
                test.testGoto();
                test.testCanonicalCollections();
            } else {
                System.out.println("Error!!! That is not a valid option!!! Please try again!");
            }
        }
    }

    public static void emptyFile(String file) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(file);
        writer.print("");
        writer.close();
    }

    public static Stack<String> readFirstElemFromFile(String filename) {
        BufferedReader reader;
        Stack<String> wordStack = new Stack<>();
        ArrayList<String> normal = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine();
            while (line != null) {
                String[] split = line.split("\\s+");
                normal.add(split[0]);
                line = reader.readLine();
            }
            for (int i = normal.size() - 1; i >= 0; i--) {
                wordStack.add(normal.get(i));
            }
            return wordStack;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeToFile(String file, String line) throws IOException {
        FileWriter fw = new FileWriter(file, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(line);
        bw.newLine();
        bw.close();
    }

    private static void printToFile(String filePath, Object object) {
        try (PrintStream printStream = new PrintStream(filePath)) {
            printStream.println(object);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Stack<String> readSequence(String filename) {
        BufferedReader reader;
        Stack<String> wordStack = new Stack<>();
        try {
            reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine();
            if (line != null) {
                Arrays.stream(new StringBuilder(line).reverse().toString().split("")).forEach(wordStack::push);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wordStack;
    }

    public static void main(String[] args) throws Exception {
        runGrammarMenu();
    }
}