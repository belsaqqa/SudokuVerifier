package sudoku;

import java.io.IOException;

public class SudokuVerifier {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java SudokuVerifier <csv-file> <mode>");
            System.err.println("Modes: 0 (sequential), 3 (3-thread), 27 (27-thread)");
            System.exit(1);
        }
        
        String csvFile = args[0];
        int mode = Integer.parseInt(args[1]);
        
        try {
            // Read board from CSV
            SudokuBoard board = SudokuBoard.fromCSV(csvFile);
            
            // Create appropriate verifier
            VerifierFactory factory = new VerifierFactory();
            VerifierStrategy verifier = factory.createVerifier(mode);
            
            // Verify and print result
            VerificationResult result = verifier.verify(board.getGrid());
            System.out.println(result.toString());
            
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}