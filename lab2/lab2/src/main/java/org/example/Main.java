package org.example;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class Main {
    public static void main(String[] args) {
        SymbolTable symbolTable = new SymbolTable(15);
        Pair<Integer, Integer> p1 = new ImmutablePair<>(-1,-1);
        Pair<Integer, Integer> p2 = new ImmutablePair<>(-1,-1);
        Pair<Integer, Integer> p3 = new ImmutablePair<>(-1,-1);

        try {
            System.out.println("\nAdding identifiers...");
            p1 = symbolTable.addIdentifier("a");
            System.out.println("\ta -> " + p1);
            System.out.println("\tb -> " + symbolTable.addIdentifier("b"));
            System.out.println("\tc -> " + symbolTable.addIdentifier("c"));
            System.out.println("\td -> " + symbolTable.addIdentifier("d"));

            System.out.println("\nAdding integer constants...");
            System.out.println("\t1 -> " + symbolTable.addIntConst(1));
            p2 = symbolTable.addIntConst(2);
            System.out.println("\t2 -> " + p2);
            System.out.println("\t3 -> " + symbolTable.addIntConst(3));
            System.out.println("\t4 -> " + symbolTable.addIntConst(4));
            System.out.println("\t5 -> " + symbolTable.addIntConst(5));
            System.out.println("\t6 -> " + symbolTable.addIntConst(6));
            System.out.println("\t7 -> " + symbolTable.addIntConst(7));
            System.out.println("\t8 -> " + symbolTable.addIntConst(8));
            System.out.println("\t9 -> " + symbolTable.addIntConst(9));
            System.out.println("\t11 -> " + symbolTable.addIntConst(11));
            System.out.println("\t12 -> " + symbolTable.addIntConst(12));
            System.out.println("\t13 -> " + symbolTable.addIntConst(13));
            System.out.println("\t100 -> " + symbolTable.addIntConst(100));

            System.out.println("\nAdding string constants...");
            System.out.println("\tana -> " + symbolTable.addStringConst("ana"));
            System.out.println("\tare -> " + symbolTable.addStringConst("are"));
            p3 = symbolTable.addStringConst("mere");
            System.out.println("\tmere -> " + p3);
            System.out.println("\tsi -> " + symbolTable.addStringConst("si"));
            System.out.println("\tmere -> " + symbolTable.addStringConst("pere"));

            System.out.println("\n");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println(symbolTable);

        try {
            System.out.println("\n");
            System.out.println("Integer constant: 100 -> " + symbolTable.getIntConstPos(100));
        } catch (AssertionError e) {
            System.out.println(e.getMessage());
        }

        try {
            System.out.println("Identifier: vvv -> " + symbolTable.getIdentifierPos("vvv"));
            System.out.println("            a -> " + symbolTable.getIdentifierPos("a"));
        } catch (AssertionError e) {
            System.out.println(e.getMessage());
        }

        try {
            System.out.println("String constant: mere -> " + symbolTable.getStringConstPos("mere"));
        } catch (AssertionError e) {
            System.out.println(e.getMessage());
        }
    }
}