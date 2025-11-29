package sudoku;

import java.util.*;
import java.util.concurrent.Callable;

public class ValidationWorker implements Callable<List<ValidationError>> {
    private ValidationStrategy validator;
    private int index;
    private int[][] board;
    
    public ValidationWorker(ValidationStrategy validator, int index, int[][] board) {
        this.validator = validator;
        this.index = index;
        this.board = board;
    }
    
    @Override
    public List<ValidationError> call() {
        // Call the validator (returns null if no errors)
        ValidationError error = validator.validate(board, index);
        
        //Checking if there are errors
        List<ValidationError> errors = new ArrayList<>();
        if (error != null && error.hasErrors()) {
            errors.add(error);
        }
        return errors;
    }
}
