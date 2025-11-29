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
        // Call the validator (returns null if no errors)
        ValidationError error = validator.validate(board, index);
        
        // If error exists, add to shared collection
        if (error != null && error.hasErrors()) {
            errorCollection.add(error);
        }
        
        return null; // We're using Void, so return null
    }
}
