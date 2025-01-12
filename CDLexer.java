package compilerproject;
import java.io.*;
import java.util.*;
public class CDLexer {
private static final Map<String, String> TOKEN = new HashMap<>();

    static {
        TOKEN.put("Fact", "keyword");
        TOKEN.put("Subt", "keyword");
        TOKEN.put("Sum", "keyword");
        TOKEN.put("abstract", "keyword");
        TOKEN.put("assert", "keyword");
        TOKEN.put("boolean", "keyword");
        TOKEN.put("break", "keyword");
        TOKEN.put("byte", "keyword");
        TOKEN.put("case", "keyword");
        TOKEN.put("catch", "keyword");
        TOKEN.put("char", "keyword");
        TOKEN.put("class", "keyword");
        TOKEN.put("const", "keyword");
        TOKEN.put("continue", "keyword");
        TOKEN.put("default", "keyword");
        TOKEN.put("do", "keyword");
        TOKEN.put("double", "keyword");
        TOKEN.put("else", "keyword");
        TOKEN.put("enum", "keyword");
        TOKEN.put("extends", "keyword");
        TOKEN.put("final", "keyword");
        TOKEN.put("finally", "keyword");
        TOKEN.put("float", "keyword");
        TOKEN.put("for", "keyword");
        TOKEN.put("if", "keyword");
        TOKEN.put("implements", "keyword");
        TOKEN.put("import", "keyword");
        TOKEN.put("instanceof", "keyword");
        TOKEN.put("int", "keyword");
        TOKEN.put("interface", "keyword");
        TOKEN.put("long", "keyword");
        TOKEN.put("native", "keyword");
        TOKEN.put("new", "keyword");
        TOKEN.put("package", "keyword");
        TOKEN.put("private", "keyword");
        TOKEN.put("protected", "keyword");
        TOKEN.put("public", "keyword");
        TOKEN.put("return", "keyword");
        TOKEN.put("short", "keyword");
        TOKEN.put("static", "keyword");
        TOKEN.put("strictfp", "keyword");
        TOKEN.put("super", "keyword");
        TOKEN.put("switch", "keyword");
        TOKEN.put("synchronized", "keyword");
        TOKEN.put("this", "keyword");
        TOKEN.put("throw", "keyword");
        TOKEN.put("throws", "keyword");
        TOKEN.put("transient", "keyword");
        TOKEN.put("try", "keyword");
        TOKEN.put("void", "keyword");
        TOKEN.put("volatile", "keyword");
        TOKEN.put("while", "keyword");
        TOKEN.put("true", "boolean_literal");
        TOKEN.put("false", "boolean_literal");
        TOKEN.put("null", "null_literal");
        TOKEN.put("==", "operator");
        TOKEN.put("!=", "operator");
        TOKEN.put("<", "operator");
        TOKEN.put("<=", "operator");
        TOKEN.put(">", "operator");
        TOKEN.put(">=", "operator");
        TOKEN.put("&&", "operator");
        TOKEN.put("||", "operator");
        TOKEN.put("!", "operator");
        TOKEN.put("&", "operator");
        TOKEN.put("|", "operator");
        TOKEN.put("^", "operator");
        TOKEN.put("<<", "operator");
        TOKEN.put(">>", "operator");
        TOKEN.put("++", "operator");
        TOKEN.put("--", "operator");
        TOKEN.put("+", "operator");
        TOKEN.put("-", "operator");
        TOKEN.put("*", "operator");
        TOKEN.put("/", "operator");
        TOKEN.put("%", "operator");
        TOKEN.put("=", "operator");
        TOKEN.put("+=", "operator");
        TOKEN.put("-=", "operator");
        TOKEN.put("*=", "operator");
        TOKEN.put("/=", "operator");
        TOKEN.put("%=", "operator");
        TOKEN.put("<<=", "operator");
        TOKEN.put(">>=", "operator");
        TOKEN.put("&=", "operator");
        TOKEN.put("|=", "operator");
        TOKEN.put("^=", "operator");
        TOKEN.put("(", "left_parenthesis");
        TOKEN.put(")", "right_parenthesis");
        TOKEN.put("{", "left_brace");
        TOKEN.put("}", "right_brace");
        TOKEN.put("[", "left_bracket");
        TOKEN.put("]", "right_bracket");
        TOKEN.put(";", "semicolon");
        TOKEN.put(",", "comma");
        TOKEN.put(".", "dot");
        TOKEN.put("identifier", "identifier");
        TOKEN.put("integer_literal", "literal");
        TOKEN.put("floating_point_literal", "literal");
        TOKEN.put("string_literal", "literal");
    }

    public List<String> extractTokens(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();
        boolean inString = false;

        for (char character : line.toCharArray()) {
            if (inString) {
                if (character == '"') {
                    inString = false;
                    currentToken.append(character);
                    tokens.add(currentToken.toString());
                    currentToken.setLength(0);
                } else {
                    currentToken.append(character);
                }
            } else {
                if (Character.isDigit(character) || character == '.') {
                    if (currentToken.length() > 0 && !Character.isDigit(currentToken.charAt(0))) {
                        tokens.add(currentToken.toString());
                        currentToken.setLength(0);
                    }
                    currentToken.append(character);
                } else if (Character.isLetter(character) || character == '_') {
                    currentToken.append(character);
                } else if (isSeparator(character)) {
                    if (currentToken.length() > 0) {
                        tokens.add(currentToken.toString());
                        currentToken.setLength(0);
                    }
                    if (!Character.isWhitespace(character)) {
                        tokens.add(String.valueOf(character));
                    }
                } else {
                    currentToken.append(character);
                }
            }

            if (character == '"') {
                inString = !inString;
            }
        }

        if (currentToken.length() > 0) {
            tokens.add(currentToken.toString());
        }

        return tokens;
    }

    private boolean isSeparator(char character) {
        return Character.isWhitespace(character)
                || "(){}[],.;".indexOf(character) != -1
                || "+-*/=%<>&|^!~".indexOf(character) != -1;
    }

    public void logToken(String token, int lineNumber, FileWriter writer) throws IOException {
        String tokenType = identifyTokenType(token);
        writer.write(token + "\t" + tokenType + "\t" + lineNumber + "\n");
    }

    public String identifyTokenType(String token) {
        if (TOKEN.containsKey(token)) {
            return TOKEN.get(token);
        } else if (token.matches("[0-9]+")) {
            return "integer_literal";
        } else if (token.matches("[0-9]*\\.[0-9]+")) {
            return "floating_point_literal";
        } else if (token.matches("\".*\"")) {
            return "string_literal";
        } else if (token.matches("[a-zA-Z_$][a-zA-Z\\d_$]*")) {
            return "identifier";
        } else {
            return "unknown";
        }
    }

    public boolean isKeyword(String token) {
        return TOKEN.containsKey(token) && "keyword".equals(TOKEN.get(token));
    }
}
 

