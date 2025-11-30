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
public ValidationError validate(int board[][], int col) {
    Map<Integer, Set<Integer>> valueToPositions = new HashMap<>();
    Set<Integer> allNumbers = new HashSet<>();

    // Collect all positions for each value
    for (int row = 0; row < 9; row++) {
        int value = board[row][col];

        if (value >= 1 && value <= 9) {
            valueToPositions.computeIfAbsent(value, k -> new HashSet<>()).add(row);
            allNumbers.add(value);
        }
    }

    // Find missing numbers
    Set<Integer> missingNumbers = numberValidator.findMissingNumbers(allNumbers);
    
    // Only return error if there are actual issues
    if (valueToPositions.values().stream().noneMatch(positions -> positions.size() > 1) 
        && missingNumbers.isEmpty()) {
        return null;
    }
    
    return new ValidationError(
        "COL",
        col,
        valueToPositions,
        missingNumbers
    );
}
    
    
    
    
           
        }
    