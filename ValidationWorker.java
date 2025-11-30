
package sudoku;

import java.util.List;
import java.util.concurrent.Callable;

public class ValidationWorker implements Callable<Void> {
    private ValidationStrategy validator;
    private int index;
    private int[][] board;
    private List<ValidationError> errorCollection;
    
    public ValidationWorker(ValidationStrategy validator, int index, int[][] board, List<ValidationError> errorCollection) {
        this.validator = validator;
        this.index = index;
        this.board = board;
        this.errorCollection = errorCollection;
    }
    
    @Override
    public Void call() {
        try {
            // Call the validator (returns null if no errors)
            ValidationError error = validator.validate(board, index);
            
            // If error exists, add to shared collection
            if (error != null) {
                errorCollection.add(error);
            }
        } catch (Exception e) {
            System.err.println("Validation error in worker: " + e.getMessage());
        }
        
        return null;
    }
}





