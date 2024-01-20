package Scanner;

import Utils.Pair;
import lombok.Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Data
public class MyScanner {
    private static final Logger LOGGER = Logger.getLogger(MyScanner.class.getName());

    private final ArrayList<String> operators = new ArrayList<>(
            List.of("+", "-", "*", "/", "%", "<=", ">=", "==", "!=", "<", ">", "=")
    );

    private final ArrayList<String> separators = new ArrayList<>(
            List.of("{", "}", "(", ")", "[", "]", ":", ";", " ", ",", "\t", "\n", "'", "\"")
    );

    private final ArrayList<String> keywords = new ArrayList<>(
            List.of("mul", "div", "mod", "add", "sub", "read", "print", "if", "else", "for", "while", "int",
                    "string", "char", "return", "start", "array")
    );

    private final String filePath;

    private SymbolTable symbolTable;

    private PIF pif;

    public MyScanner(String filePath) {
        this.filePath = filePath;
        this.symbolTable = new SymbolTable(100);
        this.pif = new PIF();
    }

    /**
     * This method reads the content of the file and ignores the spaces, deleting them
     *
     * @return - content of the file
     * @throws FileNotFoundException if the file doesn't exist
     */
    private String readFile() throws FileNotFoundException {
        StringBuilder fileContent = new StringBuilder();
        Scanner scanner = new Scanner(new File(this.filePath));
        while (scanner.hasNextLine()) {
            fileContent.append(scanner.nextLine()).append("\n");
        }

        return fileContent.toString().replace("\t", "");
    }

    private List<Pair<String, Pair<Integer, Integer>>> createListOfProgramsElements() {
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
            LOGGER.log(Level.SEVERE, "Error creating the list of program elements: " + filePath, e);
        }

        return null;
    }

    /**
     * This method goes through each string from tokensToBe and looks in what case are we (4 cases):
     * 1) we are managing a string
     * - we are either at the start of the string and we start to create it
     * - we found the end of the string so we addToST it to our final list + the line and the column on which
     * it is situated
     * 2) we are managing a char
     * - we are either at the start of the char and we start to create it
     * - we found the end of the char so we addToST it to our final list + the line and the column on which
     * it is situated
     * 3) we have a new line
     * - we increase the line number
     * - we make the number column 1 again
     * 4) the case when:
     * - if we have a string, we compute the string
     * - if we have a char, we compute the char
     * - if the token is different from a space, it means we found a token and we addToST it to our final
     * list + the line and the column on which it is situated, and we increase the column number
     *
     * @param tokensToBe - list of program elements (strings) + the separators
     * @return - list of pairs composed of tokens/identifiers/constants + a pair which is composed of the number of the
     * line and the number of column on which them were placed
     */
    private List<Pair<String, Pair<Integer, Integer>>> tokenize(List<String> tokensToBe) {

        List<Pair<String, Pair<Integer, Integer>>> resultedTokens = new ArrayList<>();
        boolean isStringConstant = false;
        boolean isCharConstant = false;
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
                case "'":
                    if (isCharConstant) {
                        createdString.append(t);
                        resultedTokens.add(new Pair<>(createdString.toString(), new Pair<>(numberLine, numberColumn)));
                        createdString = new StringBuilder();
                    } else {
                        createdString.append(t);
                    }
                    isCharConstant = !isCharConstant;
                    break;
                case "\n":
                    numberLine++;
                    numberColumn = 1;
                    break;
                default:
                    if (isStringConstant) {
                        createdString.append(t);
                    } else if (isCharConstant) {
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
     * This method scans the list of created tokens, and classifies each of them in a category:
     *      - 0 - constants
     *      - 1 - identifiers
     *      - 2 - keywords
     *      - 3 - operators
     *      - 4 - separators
     * If the token is a constant or an identifier we addToST it to the Symbol Table.
     * After figuring out the category, we addToST them to the PIF + their position in the symbol table
     * ( (-1, -1) for anything that is not a constant and an identifier ) + their category (0, 1, 2, 3, 4)
     * If the token is not in any of the categories, we print a message with the line and the column of the error
     * + the token which is invalid.
     */
    public void scan() {
        List<Pair<String, Pair<Integer, Integer>>> tokens = createListOfProgramsElements();
        AtomicBoolean lexicalErrorExists = new AtomicBoolean(false);

        if (tokens == null) {
            return;
        }

        tokens.forEach(t -> {
            String token = t.getFirst();
            if (this.keywords.contains(token)) { // the token is a constant
                this.pif.add(new Pair<>(token, new Pair<>(-1, -1)), 2);
            } else if (this.operators.contains(token)) { // the token is an operator
                this.pif.add(new Pair<>(token, new Pair<>(-1, -1)), 3);
            } else if (this.separators.contains(token)) { // the token is a separator
                this.pif.add(new Pair<>(token, new Pair<>(-1, -1)), 4);
            } else if (Pattern.compile("^0|[-|+][1-9]([0-9])*|'[1-9]'|'[a-zA-Z]'|\"[0-9]*[a-zA-Z ]*\"$")
                    .matcher(token).matches()) { // the token is a constant
                this.symbolTable.addToST(token);
                this.pif.add(new Pair<>("CONST", symbolTable.findPositionOfTerm(token)), 0);
            } else if (Pattern.compile("^([a-zA-Z]|_)|[a-zA-Z_0-9]*")
                    .matcher(token).matches()) { // the token is an identifier
                this.symbolTable.addToST(token);
                this.pif.add(new Pair<>("IDENTIFIER", symbolTable.findPositionOfTerm(token)), 1);
            } else {
                Pair<Integer, Integer> pairLineColumn = t.getSecond();
                System.out.println("Error at line: " + pairLineColumn.getFirst() + " and column: "
                        + pairLineColumn.getSecond() + ", invalid token: " + t.getFirst());
                lexicalErrorExists.set(true);
            }
        });

        if (!lexicalErrorExists.get()) {
            System.out.println("Program is lexically correct!");
        }

    }
}
