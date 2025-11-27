package sudokulab9;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ColumnVerifier implements Runnable {

    private final SudokuBoard board;                    // final for thread safety
    private final List<ValidationError> sharedErrors;
    
     public ColumnVerifier(SudokuBoard board, List<ValidationError> sharedErrors){
        this.board = board;
        this.sharedErrors = sharedErrors;
    }
    

    @Override
    public void run() {
        for (int col = 0; col < 9; col++) {  // sending each column to get validated
            validateSingleColumn(col);
        }
    }
    
    private void validateSingleColumn(int col) {
        Map<Integer, Set<Integer>> valuePositions = new HashMap<>(); 
        //digit itself, a set of row numbers of where it is duplicated
        
        for (int row = 0; row < 9; row++) {
            int value = board.getCell(row, col);
            valuePositions.putIfAbsent(value, new HashSet<>());// if the value encountered is already in hashmap dont addit if it is not add it and create a hashset to store its duplicates
            valuePositions.get(value).add(row);
        }

        for (int row = 0; row < 9; row++) {
            int value = board.getCell(row, col);
            
            if (value < 1 || value > 9) {
                synchronized(sharedErrors) {
                    sharedErrors.add(new ValidationError());  // assuming it stores an error 
                }
                continue;
            }
            
            if (valuePositions.get(value).size() > 1) {
                synchronized(sharedErrors) {
                    sharedErrors.add(new ValidationError());// storing the error useing the validationerror class
                }
                break;
            }
        }
    

    }
}
