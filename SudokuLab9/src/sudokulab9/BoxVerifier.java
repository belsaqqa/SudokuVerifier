package sudokulab9;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.*;

public class BoxVerifier implements ValidationStrategy {

    private final NumberValidator numberValidator;

    public BoxVerifier(NumberValidator numberValidator) {

        this.numberValidator = numberValidator;
    }

    @Override
    public ValidationError validate(int board[][], int index) {
        Set<Integer> numbers = new HashSet<>();
        Set<Integer> duplicateValues = new HashSet<>();
        Set<Integer> duplicatePositions = new HashSet<>();

        // Calculate box coordinates (3x3 grid) for 1-based box number
        int boxIndex = index - 1;  // Convert to 0-based for calculations
        int startRow = (boxIndex / 3) * 3;  // 0, 0, 0, 3, 3, 3, 6, 6, 6
        int startCol = (boxIndex % 3) * 3;  // 0, 3, 6, 0, 3, 6, 0, 3, 6

        // Check all 9 cells in the 3x3 box
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int row = startRow + i;
                int col = startCol + j;
                int value = board[row][col];

                // Check if value is in valid range
                if (value < 1 || value > 9) {
                    continue;
                }

                // calculate position within box (1-9)
                int positionInBox = (i * 3) + j + 1; // 1-based position

                // Check for duplicates
                if (!numbers.add(value)) {
                    duplicateValues.add(value);
                    duplicatePositions.add(positionInBox);
                }
            }
        }

        // Find missing numbers
        Set<Integer> missingNumbers = numberValidator.findMissingNumbers(numbers);

        if (duplicateValues.isEmpty() && missingNumbers.isEmpty()) {
            return null;
        }

        return new ValidationError(
                "BOX",
                index, // 1-based box number (1-9) not zero
                duplicateValues,
                duplicatePositions,
                missingNumbers
        );

    }
}
