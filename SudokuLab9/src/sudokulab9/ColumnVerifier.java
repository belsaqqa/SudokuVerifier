package sudokulab9;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ColumnVerifier implements Runnable {
    private final int[][] board;
    private final List<ValidationError> sharedErrors;
    private final NumberValidator numberValidator;
    
    public ColumnVerifier(int[][] board, List<ValidationError> sharedErrors, NumberValidator numberValidator) {
        this.board = board;
        this.sharedErrors = sharedErrors;
        this.numberValidator = numberValidator;
    }
    
    @Override
    public void run() {
        for (int col = 0; col < 9; col++) {
            validateSingleColumn(col);
        }
    }
    
    private void validateSingleColumn(int col) {
        Set<Integer> numbers = new HashSet<>();
        Set<Integer> duplicateValues = new HashSet<>();
        Set<Integer> duplicatePositions = new HashSet<>();
        
        // Check for duplicates and collect all numbers
        for (int row = 0; row < 9; row++) {
            int value = board[row][col];
            
            if (value < 1 || value > 9) {
                continue;
            }
            
            if (!numbers.add(value)) {
                duplicateValues.add(value);
                duplicatePositions.add(row + 1); // 1-based position
            }
        }
        
        // Find missing numbers
        Set<Integer> missingNumbers = numberValidator.findMissingNumbers(numbers);
        
        // Create error if needed
        if (!duplicateValues.isEmpty() || !missingNumbers.isEmpty()) {
            ValidationError error = new ValidationError(
                "COL", 
                col, 
                duplicateValues, 
                duplicatePositions, 
                missingNumbers
            );
            
            synchronized(sharedErrors) {
                sharedErrors.add(error);
            }
        }
    }
}