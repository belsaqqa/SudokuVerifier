package sudokulab9;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.*;

public class RowVerifier implements Runnable {

    private final int[][] board;
    private final List<ValidationError> sharedErrors;
    private final NumberValidator numberValidator;

    public RowVerifier(int[][] board, List<ValidationError> sharedErrors, NumberValidator numberValidator) {
        this.board = board;
        this.sharedErrors = sharedErrors;
        this.numberValidator = numberValidator;
    }

    @Override
    public void run() {
        for (int row = 0; row < 9; row++) {
            validateSingleRow(row);
        }
    }

    private void validateSingleRow(int row) {
        Set<Integer> numbers = new HashSet<>();
        Set<Integer> duplicateValues = new HashSet<>();
        Set<Integer> duplicatePositions = new HashSet<>();

        // First pass: Check for duplicates and collect all numbers
        for (int col = 0; col < 9; col++) {
            int value = board[row][col];

            // Check if value is in valid range
            if (value < 1 || value > 9) {
                // This should be handled by CSV reader
                continue;
            }

            // Check for duplicates
            if (!numbers.add(value)) {
                duplicateValues.add(value);
                duplicatePositions.add(col + 1); // 1-based position
            }
        }

        // Find missing numbers
        Set<Integer> missingNumbers = numberValidator.findMissingNumbers(numbers);

        // If there are any errors, create ValidationError and add to shared list
        if (!duplicateValues.isEmpty() || !missingNumbers.isEmpty()) {
            ValidationError error = new ValidationError(
                    "ROW",
                    row,
                    duplicateValues,
                    duplicatePositions,
                    missingNumbers
            );

            synchronized (sharedErrors) {
                sharedErrors.add(error);
            }
        }
    }
}
