package compilerproject;
import java.io.*;
import java.util.*;
public class CDParser {
    public void analyzeTokens(String inputFilePath, Set<String> declaredVariables) throws IOException {
        Scanner fileScanner = new Scanner(new File(inputFilePath));

        Stack<Character> parenthesisStack = new Stack<>();
        Stack<Character> braceStack = new Stack<>();
        int lineNumber = 1;
        boolean isInIfStatement = false;
        boolean isInLoop = false;

        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine().trim();
            String[] tokens = line.split("\\s+");

            if (line.isEmpty()) {
                lineNumber++;
                continue;
            }

            if (!line.endsWith("{") && !line.endsWith("}") && !line.endsWith(";") && !line.endsWith(")")) {
                System.err.println("Error on line " + lineNumber + ": Missing semicolon.");
            }

            if (tokens.length > 1 && new CDLexer().isKeyword(tokens[0])) {
                String[] parts = tokens[1].split(";");
                if (parts.length > 0 && parts[0].matches("[a-zA-Z_$][a-zA-Z\\d_$]*")) {
                    String variableName = parts[0];
                    if (declaredVariables.contains(variableName)) {
                        System.err.println("Error on line " + lineNumber + ": Redefined variable '" + variableName + "'.");
                    } else {
                        declaredVariables.add(variableName);
                    }
                } else {
                    System.err.println("Error on line " + lineNumber + ": Invalid variable name '" + parts[0] + "'.");
                }
            }

            for (char character : line.toCharArray()) {
                if (character == '(') {
                    parenthesisStack.push(character);
                } else if (character == ')') {
                    if (parenthesisStack.isEmpty() || parenthesisStack.peek() != '(') {
                        System.err.println("Error on line " + lineNumber + ": Mismatched parenthesis.");
                    } else {
                        parenthesisStack.pop();
                    }
                } else if (character == '{') {
                    braceStack.push(character);
                } else if (character == '}') {
                    if (braceStack.isEmpty() || braceStack.peek() != '{') {
                        System.err.println("Error on line " + lineNumber + ": Mismatched brace.");
                    } else {
                        braceStack.pop();
                    }
                }
            }

            for (String token : tokens) {
                if (new CDLexer().isKeyword(token.toLowerCase())) {
                    String actualKeyword = token;
                    String expectedKeyword = token.toLowerCase();
                    if (!actualKeyword.equals(expectedKeyword)) {
                        System.err.println("Error on line " + lineNumber + ": Keyword '" + actualKeyword + "' should be lowercase.");
                    }
                }
            }

            if (tokens.length > 0 && tokens[0].equals("if")) {
                isInIfStatement = true;
                if (!line.contains("(") || !line.contains(")")) {
                    System.err.println("Error on line " + lineNumber + ": 'if' statement should have parentheses.");
                }
                if (line.contains(")")) {
                    int closingParenthesisIndex = line.indexOf(")");
                    if (closingParenthesisIndex + 1 < line.length() && line.charAt(closingParenthesisIndex + 1) != '{') {
                        System.err.println("Error on line " + lineNumber + ": 'if' statement should be followed by '{'.");
                    }
                }
            } else if (isInIfStatement && !line.contains("{")) {
                System.err.println("Error on line " + lineNumber + ": Expected '{' after 'if' statement.");
                isInIfStatement = false;
            }

            if (tokens.length > 0 && (tokens[0].equals("for") || tokens[0].equals("while") || tokens[0].equals("do"))) {
                isInLoop = true;
                if (!line.contains("(") || !line.contains(")")) {
                    System.err.println("Error on line " + lineNumber + ": Loop statement should have parentheses.");
                }
                if (line.contains(")")) {
                    int closingParenthesisIndex = line.indexOf(")");
                    if (closingParenthesisIndex + 1 < line.length() && line.charAt(closingParenthesisIndex + 1) != '{') {
                        System.err.println("Error on line " + lineNumber + ": Loop statement should be followed by '{'.");
                    }
                }
            } else if (isInLoop && !line.contains("{")) {
                System.err.println("Error on line " + lineNumber + ": Expected '{' after loop declaration.");
                isInLoop = false;
            }

            lineNumber++;
        }
        fileScanner.close();

        while (!parenthesisStack.isEmpty()) {
            System.err.println("Error: Missing closing parenthesis for '(' opened earlier.");
            parenthesisStack.pop();
        }
        while (!braceStack.isEmpty()) {
            System.err.println("Error: Missing closing brace for '{' opened earlier.");
            braceStack.pop();
        }
    }
}