package sudokulab9;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.*;

public class RowVerifier implements ValidationStrategy {

    private final NumberValidator numberValidator;

    public RowVerifier(NumberValidator numberValidator) {

        this.numberValidator = numberValidator;
    }

    @Override
    public ValidationError validate(int board[][], int row) {
        Set<Integer> numbers = new HashSet<>();
        Set<Integer> duplicateValues = new HashSet<>();
        Set<Integer> duplicatePositions = new HashSet<>();

        // First pass: Check for duplicates and collect all numbers
        for (int col = 0; col < 9; col++) {
            int value = board[row][col];

            // Check if value is in valid range (should probabaly remove this)
            if (value < 1 || value > 9) {
                // This should be handled by CSV reader
                continue;
            }

            // Check for duplicates
            if (!numbers.add(value)) {
                duplicateValues.add(value);
                duplicatePositions.add(col + 1);
            }
        }

        // Find missing numbers
        Set<Integer> missingNumbers = numberValidator.findMissingNumbers(numbers);
        
        boolean hasError = !duplicateValues.isEmpty() || !missingNumbers.isEmpty();

        if(!hasError){
            return null; // as there is no error
        }
        
        return new ValidationError(
                "ROW",
                 row,
                 duplicateValues,
                 duplicatePositions,
                 missingNumbers
            );

        }

    }
