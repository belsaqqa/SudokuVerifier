package sudoku;

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
        Map<Integer, Set<Integer>> valueToPositions = new HashMap<>();
        Set<Integer> allNumbers = new HashSet<>();

        // Collect all positions for each value
        for (int col = 0; col < 9; col++) {
            int value = board[row][col];

            if (value >= 1 && value <= 9) {
                valueToPositions.computeIfAbsent(value, k -> new HashSet<>()).add(col);
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
                "ROW",
                row,
                valueToPositions,
                missingNumbers
        );
    }

}
