package org.example;

import java.io.FileNotFoundException;
import java.io.PrintStream;

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

    public static void main(String[] args) {
        run("/Users/irinascortar/Desktop/JavaProjects/SEM5_FLCD/lab3/lab3/src/main/java/org/example/IO/p1/p1.txt");
        run("/Users/irinascortar/Desktop/JavaProjects/SEM5_FLCD/lab3/lab3/src/main/java/org/example/IO/p2/p2.txt");
        run("/Users/irinascortar/Desktop/JavaProjects/SEM5_FLCD/lab3/lab3/src/main/java/org/example/IO/p3/p3.txt");
        run("/Users/irinascortar/Desktop/JavaProjects/SEM5_FLCD/lab3/lab3/src/main/java/org/example/IO/p1err/p1err.txt");
        run("/Users/irinascortar/Desktop/JavaProjects/SEM5_FLCD/lab3/lab3/src/main/java/org/example/IO/p4/p4.txt");
    }
}