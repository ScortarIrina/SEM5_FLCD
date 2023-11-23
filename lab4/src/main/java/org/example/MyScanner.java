package org.example;


import lombok.Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Data
public class MyScanner {

    private final ArrayList<String> operators = new ArrayList<>(
            List.of("+", "-", "*", "/", "%", "<=", ">=", "==", "!=", "<", ">", "=")
    );

    private final ArrayList<String> separators = new ArrayList<>(
            List.of("{", "}", "(", ")", "[", "]", ":", ";", " ", ",", "\t", "\n", "\"")
    );

    private final ArrayList<String> keywords = new ArrayList<>(
            List.of("read", "print", "if", "else", "while", "int", "string", "return", "array")
    );

    private final String filePath;

    private SymbolTable symbolTable;

    private ProgramInternalForm pif;

    public MyScanner(String filePath) {
        this.filePath = filePath;
        this.symbolTable = new SymbolTable(100);
        this.pif = new ProgramInternalForm();
    }

    private String readFile() throws FileNotFoundException {
        StringBuilder fileContent = new StringBuilder();
        Scanner scanner = new Scanner(new File(this.filePath));
        while (scanner.hasNextLine()) {
            fileContent.append(scanner.nextLine()).append("\n");
        }

        // remove the tabs from the file when reading its content
        return fileContent.toString().replace("\t", "");
    }


    /**
     * This method is responsible for building the initial setup required for tokenization.
     * It includes reading the file content, consolidating separators into a single string, and dividing the program
     * elements into a list.
     *
     * @return - the list of pairs composed of tokens/identifiers/constants + a pair which is composed of the number of
     * the line and the number of column on which them were placed
     */
    private List<Pair<String, Pair<Integer, Integer>>> createListOfProgramsElems() {
        try {
            String content = this.readFile();
            String separatorsString = this.separators.stream().reduce("", (a, b) -> (a + b));
            StringTokenizer tokenizer = new StringTokenizer(content, separatorsString, true);

            List<String> tokens = Collections.list(tokenizer)
                    .stream()
                    .map(t -> (String) t)
                    .collect(Collectors.toList());

            return tokenize(tokens);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * This method is responsible for parsing program elements to differentiate between tokens and separators,
     * while also tracking their positions within the program structure.
     * <p>
     * 1) String Management:
     *      - identifies and handles string cases:
     *              - begins string creation when encountering the start of a string
     *              - completes and includes strings in the final list when the end of a string is detected
     * 2) New Line Handling:
     *      - increases the line count when a new line is encountered
     *      - resets the column count to 1 for the start of a new line
     * 3) Token recognition for non-string cases:
     *      - tracks tokens and adds them to the final list
     *      - records their position, including line and column numbers
     *      - increments the column number for each token encountered (excluding spaces)
     * <p>
     * @param tokensToBe - the List of program elements (strings) + the separators
     * @return - list of pairs containing identified tokens and their respective line and column positions
     */
    private List<Pair<String, Pair<Integer, Integer>>> tokenize(List<String> tokensToBe) {

        List<Pair<String, Pair<Integer, Integer>>> resultedTokens = new ArrayList<>();
        boolean isStringConstant = false;
        StringBuilder createdString = new StringBuilder();
        int numberLine = 1;
        int numberColumn = 1;

        for (String t : tokensToBe) {
            switch (t) {
                case "\"":
                    createdString.append(t);
                    if (isStringConstant) {
                        resultedTokens.add(new Pair<>(createdString.toString(), new Pair<>(numberLine, numberColumn)));
                        createdString = new StringBuilder();
                    }
                    isStringConstant = !isStringConstant;
                    break;
                case "\n":
                    numberLine++;
                    numberColumn = 1;
                    break;
                default:
                    if (isStringConstant) {
                        createdString.append(t);
                    } else if (!t.equals(" ")) {
                        resultedTokens.add(new Pair<>(t, new Pair<>(numberLine, numberColumn)));
                        numberColumn++;
                    }
                    break;
            }
        }
        return resultedTokens;
    }

    /**
     * In this method, we scan the list of created tokens, and we classify each of them in a category:
     * a) 0 - constants
     * b) 1 - identifiers
     * c) 2 - keywords
     * d) 3 - operators
     * e) 4 - separators
     * <p>
     * If the token is a constant or an identifier we add it to the ST.
     * After figuring out the category, we add them to the PIF + their position in the ST.
     * ( (-1, -1) for anything that is not a constant and an identifier ) + their category (0, 1, 2, 3, 4)
     * If the token is not in any of the categories, we print a message with the line and the column of the error +
     * the token which is invalid.
     */
    public void scan() {
        List<Pair<String, Pair<Integer, Integer>>> tokens = createListOfProgramsElems();
        AtomicBoolean lexicalErrorExists = new AtomicBoolean(false);

        if (tokens == null) {
            return;
        }

        tokens.forEach(tokenInfo -> {
            String token = tokenInfo.getFirst();
            Pair<Integer, Integer> lineColumn = tokenInfo.getSecond();

            if (this.keywords.contains(token)) {
                addToPIF(token, new Pair<>(-1, -1), 2); // Add keyword to PIF with category 2 (keywords)
            } else if (this.operators.contains(token)) {
                addToPIF(token, new Pair<>(-1, -1), 3); // Add operator to PIF with category 3 (operators)
            } else if (this.separators.contains(token)) {
                addToPIF(token, new Pair<>(-1, -1), 4); // Add separator to PIF with category 4 (separators)
            } else if (isConstant(token)) {
                handleToken(token, lineColumn, 0); // Handle constants
            } else if (isIdentifier(token)) {
                handleToken(token, lineColumn, 1); // Handle identifiers
            } else {
                handleLexicalError(token, lineColumn); // Handle lexical errors
                lexicalErrorExists.set(true);
            }
        });


        printLexicalAnalysisResult(lexicalErrorExists);
    }

    private void addToPIF(String token, Pair<Integer, Integer> position, int category) {
        this.pif.addToPIF(new Pair<>(token, position), category);
    }

    private boolean isConstant(String token) {
        return Pattern
                .compile("^0|[-+]?[1-9][0-9]*|'[1-9]'|'[a-zA-Z]'|\"[0-9]*[a-zA-Z ]*\"$")
                .matcher(token)
                .matches();
    }

    private boolean isIdentifier(String token) {
        return Pattern
                .compile("^([a-zA-Z]|_)|[a-zA-Z_0-9]*")
                .matcher(token)
                .matches();
    }

    private void handleToken(String token, Pair<Integer, Integer> lineColumn, int category) {
        this.symbolTable.add(token);
        addToPIF(token, lineColumn, category); // Utilizing lineColumn for the token's position
    }

    private void handleLexicalError(String token, Pair<Integer, Integer> lineColumn) {
        System.out.println("\nLEXICAL ERROR: in file \"" + filePath + "\"" +
                "\n\tline: " + lineColumn.getFirst() +
                "\n\tcolumn: " + lineColumn.getSecond() +
                "\n\tinvalid token: " + token);
    }

    private void printLexicalAnalysisResult(AtomicBoolean lexicalErrorExists) {
        if (!lexicalErrorExists.get()) {
            System.out.println("\nProgram in file \"" + filePath + "\" is lexically correct.");
        }
    }
}