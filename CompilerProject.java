package compilerproject;
import java.io.*;
import java.util.*;

public class CompilerProject {
    public static void main(String[] args) {
        String inputFilePath = "SourceCode.txt";
        String tokenOutputFilePath = "Tokens.txt";
        
        try {
            new CompilerProject().tokenizeAndAnalyzeCode(inputFilePath, tokenOutputFilePath);
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    public void tokenizeAndAnalyzeCode(String inputFilePath, String tokenOutputFilePath) throws IOException {
        Scanner fileReader = new Scanner(new File(inputFilePath));
        FileWriter tokenFileWriter = new FileWriter(tokenOutputFilePath);
        Set<String> declaredVariables = new HashSet<>();

        CDLexer scanner = new CDLexer();

        int lineNumber = 1;
        while (fileReader.hasNextLine()) {
            String line = fileReader.nextLine();
            List<String> tokens = scanner.extractTokens(line);
            for (String token : tokens) {
                scanner.logToken(token, lineNumber, tokenFileWriter);
            }
            lineNumber++;
        }
        fileReader.close();
        tokenFileWriter.close();

        System.out.println("Tokenization completed. Tokens written to " + tokenOutputFilePath);

        CDParser parser = new CDParser();
        parser.analyzeTokens(inputFilePath, declaredVariables);
    }
}

