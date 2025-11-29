package sudokulab9;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ColumnVerifier implements ValidationStrategy {
    private final NumberValidator numberValidator;
    
    public ColumnVerifier(NumberValidator numberValidator) {
       
        this.numberValidator = numberValidator;
    }
    
    @Override
    public ValidationError validate(int board [][], int col) { // col is index in uml, changed it because it's easier to make sense of it
        Set<Integer> numbers = new HashSet<>();
        Set<Integer> duplicateValues = new HashSet<>();
        Set<Integer> duplicatePositions = new HashSet<>();
        
        // Check for duplicates and collect all numbers
        for (int row = 0; row < 9; row++) {
            int value = board[row][col];
            
            if (value < 1 || value > 9) {
                continue;
            } // should be validated in csvreader??
            
            if (!numbers.add(value)) {
                duplicateValues.add(value);
                duplicatePositions.add(row + 1); // 1-based position
            }
        }
        
        // Find missing numbers
        Set<Integer> missingNumbers = numberValidator.findMissingNumbers(numbers);
         boolean hasError = !duplicateValues.isEmpty() || !missingNumbers.isEmpty();

        if(!hasError){
            return null; // as there is no error
        }
        
        return new ValidationError(
                "COL",
                 col,
                 duplicateValues,
                 duplicatePositions,
                 missingNumbers
            );
        
            
            
        }
    }