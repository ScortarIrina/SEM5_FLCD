package org.example;

import java.util.List;
import java.util.Objects;

public class Test {
    private Grammar testGrammar;
    private LR0 testLR0;

    public Test() {
        testGrammar = new Grammar("src/main/java/org/example/IO/GTest.txt");
        try {
            testLR0 = new LR0(testGrammar);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void testClosure1() {
        testLR0.getCanonicalCollectionForGrammar();
        Object[] result = testLR0.closure(new Item(testLR0.getWorkingGrammar().getStart(), testLR0.getWorkingGrammar().getAllProductionsForNonTerminal(testLR0.getGrammar().getStart()).get(0), 0)).getItems().toArray();
        assert result.length == 1;
        assert Objects.equals(result[0], new Item("S0", List.of("a", "A"), 0));
        System.out.println("Closure test 1 successful");
    }

    public void testClosure2() throws Exception {
        testGrammar = new Grammar("src/main/java/org/example/IO/GTest2.txt");
        testLR0 = new LR0(testGrammar);
        testLR0.getCanonicalCollectionForGrammar();
        Object[] result = testLR0.closure(new Item(testLR0.getWorkingGrammar().getStart(), testLR0.getWorkingGrammar().getAllProductionsForNonTerminal(testLR0.getGrammar().getStart()).get(0), 0)).getItems().toArray();
        assert result.length == 1;
        assert Objects.equals(result[0], new Item("S0", List.of("a"), 0));
        System.out.println("Closure test 2 successful");
    }

    public void testClosure3() throws Exception {
        testGrammar = new Grammar("src/main/java/org/example/IO/GTest3.txt");
        testLR0 = new LR0(testGrammar);
        testLR0.getCanonicalCollectionForGrammar();
        Object[] result = testLR0.closure(new Item(testLR0.getWorkingGrammar().getStart(), testLR0.getWorkingGrammar().getAllProductionsForNonTerminal(testLR0.getGrammar().getStart()).get(0), 0)).getItems().toArray();
        assert result.length == 3;
        assert Objects.equals(result[1], new Item("A", List.of("S"), 0));
        System.out.println("Closure test 3 successful");
    }

    public void testClosure() throws Exception {
        testClosure1();
        testClosure2();
        testClosure3();
    }

    public void testGoto1() throws Exception {
        testGrammar = new Grammar("src/main/java/org/example/IO/GTest.txt");
        testLR0 = new LR0(testGrammar);
        testLR0.getCanonicalCollectionForGrammar();
        State state = testLR0.closure(new Item(testLR0.getWorkingGrammar().getStart(), testLR0.getWorkingGrammar().getAllProductionsForNonTerminal(testLR0.getGrammar().getStart()).get(0), 0));
        State result = testLR0.goTo(state, state.getPartAfterTheDot().get(0));
        assert result.getItems().size() == 2;
        assert Objects.equals(result.getItems().toArray()[1], new Item("A", List.of("a", "b"), 0));
        System.out.println("Go To Test 1 Successful");
    }

    public void testGoto2() throws Exception {
        testGrammar = new Grammar("src/main/java/org/example/IO/GTest2.txt");
        testLR0 = new LR0(testGrammar);
        testLR0.getCanonicalCollectionForGrammar();
        State state = testLR0.closure(new Item(testLR0.getWorkingGrammar().getStart(), testLR0.getWorkingGrammar().getAllProductionsForNonTerminal(testLR0.getGrammar().getStart()).get(0), 0));
        State result = testLR0.goTo(state, state.getPartAfterTheDot().get(0));
        assert result.getItems().size() == 1;
        assert Objects.equals(result.getItems().toArray()[0], new Item("S0", List.of("a"), 1));
        System.out.println("Go To Test 2 Successful");
    }

    public void testGoto() throws Exception {
        testGoto1();
        testGoto2();
    }


    public void testCanonicalCollection1() throws Exception {
        testGrammar = new Grammar("src/main/java/org/example/IO/GTest.txt");
        testLR0 = new LR0(testGrammar);
        List<State> result = testLR0.getCanonicalCollectionForGrammar().getStates();
        assert result.size() == 6;
        assert Objects.equals(result.get(result.size() - 1).getItems().toArray()[0], new Item("A", List.of("a", "b"), 2));
        System.out.println("Canonical Test 1 Successful");
    }

    public void testCanonicalCollection2() throws Exception {
        testGrammar = new Grammar("src/main/java/org/example/IO/GTest2.txt");
        testLR0 = new LR0(testGrammar);
        List<State> result = testLR0.getCanonicalCollectionForGrammar().getStates();
        assert result.size() == 1;
        assert Objects.equals(result.get(0).getItems().toArray()[0], new Item("A", List.of("a"), 1));
        System.out.println("Canonical Test 2 Successful");
    }

    public void testCanonicalCollections() throws Exception {
        testCanonicalCollection1();
        testCanonicalCollection2();
    }

}
