package sudoku;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BoxVerifier implements ValidationStrategy {

    private final NumberValidator numberValidator;

    public BoxVerifier(NumberValidator numberValidator) {

        this.numberValidator = numberValidator;
    }

    @Override
    public ValidationError validate(int board[][], int index) {
        if (index < 0 || index >= 9) {
            throw new IllegalArgumentException("Box index must be between 0 and 8, got: " + index);
        }

        Map<Integer, Set<Integer>> valueToPositions = new HashMap<>();
        Set<Integer> allNumbers = new HashSet<>();

        int startRow = (index / 3) * 3;
        int startCol = (index % 3) * 3;

        // Collect all positions for each value
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int row = startRow + i;
                int col = startCol + j;
                int value = board[row][col];
                int positionInBox = (i * 3) + j;

                if (value >= 1 && value <= 9) {
                    valueToPositions.computeIfAbsent(value, k -> new HashSet<>()).add(positionInBox);
                    allNumbers.add(value);
                }
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
                "BOX",
                index,
                valueToPositions, // Pass the detailed map
                missingNumbers
        );
    }

}
