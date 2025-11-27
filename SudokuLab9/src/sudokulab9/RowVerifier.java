
package sudokulab9;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;



public class RowVerifier implements Runnable{
    
    private final SudokuBoard board;
    private final List<ValidationError> sharedErrors;
    
    public RowVerifier(SudokuBoard board, List<ValidationError> sharedErrors){
        this.board = board;
        this.sharedErrors = sharedErrors;
    }
    @Override
    public void run(){
        for (int row = 0; row < 9; row++) {
            validateSingleRow(row);
        }
    }
    
    private void validateSingleRow(int row){
        
   
        boolean scanned = new boolean[10];
        Map<Integer, Set<Integer>> valuePositions = new Hashmap<>();
        
        for(int col = 0; col < 9; col++){
            int value = board.getCell(row,col); // supposedly a method in the sudoku board class
            
            valuePositions.putIfAbsent(value, new HashSet<>());
            valuePositions.get(value).add(col);
        }
        
        for (int col = 0; col < 9; col++ ) {
            int value = board.getCell(row, col);
            
            if (value < 1 || value > 9) {
                synchronized(sharedErrors) {
                    sharedErrors.add(new ValidationError());
                }
                continue;
            }
            
            if (valuePositions.get(value).size() > 1) {
                synchronized(sharedErrors) {
                    sharedErrors.add(new ValidationError());
                }
                break;
            }
    }
    
    
}

}
