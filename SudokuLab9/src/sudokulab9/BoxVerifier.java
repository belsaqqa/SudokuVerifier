package sudokulab9;
import java.util.HashSet;
import java.util.Set;

public class BoxVerifier implements ValidationStrategy {

    private final NumberValidator numberValidator;

    public BoxVerifier(NumberValidator numberValidator) {

        this.numberValidator = numberValidator;
    }

    @Override
    public ValidationError validate(int board[][], int index) {
        //validate boxIndex range first
        
        if (index < 0 || index >= 9) {
            throw new IllegalArgumentException("Box index must be between 0 and 8, got: "+ index);
        }
        
        Set<Integer> numbers = new HashSet<>();
        Set<Integer> duplicateValues = new HashSet<>();
        Set<Integer> duplicatePositions = new HashSet<>();

        // Calculate box coordinates (3x3 grid) for 1-based box number
        
        int startRow = (index / 3) * 3;  
        int startCol = (index % 3) * 3;
        
        // Validate bounds
        if (startRow < 0 || startRow >= 9 || startCol < 0 || startCol >= 9) {
            throw new IllegalArgumentException("Invalid box coordinates for index " + index +": startRow=" + startRow + ", startCol=" + startCol);
        }

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

                // calculate position within box 0-8
                int positionInBox = (i * 3) + j;

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
            return null; // better error handling
        }


        return new ValidationError(
                "BOX",
                index, 
                duplicateValues,
                duplicatePositions,
                missingNumbers
        );

    }
}