package sudoku;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.*;
import java.util.ArrayList;


public class ThreeThreadVerifier implements VerifierStrategy {
    private final NumberValidator numberValidator;
    private final ValidationStrategy rowValidator;
    private final ValidationStrategy colValidator;
    private final ValidationStrategy boxValidator;
    
    public ThreeThreadVerifier(NumberValidator numberValidator) {
        this.numberValidator = numberValidator;
        this.boxValidator = new BoxVerifier(numberValidator);
        this.colValidator = new ColumnVerifier(numberValidator);
        this.rowValidator = new RowVerifier(numberValidator);
    }
    
    @Override
    public VerificationResult verify(int[][] board) {
       List<ValidationError> rowErrors = new ArrayList<>();
       List<ValidationError> colErrors = new ArrayList<>();
       List<ValidationError> boxErrors = new ArrayList<>();
       
       Thread rowThread = new Thread(() -> {
            rowErrors.addAll(validateRows(board));
        });

        Thread colThread = new Thread(() -> {
            colErrors.addAll(validateColumns(board));
        });

        Thread boxThread = new Thread(() -> {
            boxErrors.addAll(validateBoxes(board));
        });
        
        rowThread.start();
        colThread.start();
        boxThread.start();
        
        try {
            rowThread.join();
            colThread.join();
            boxThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread execution interrupted", e);
        }
        
        List<ValidationError> allErrors = new ArrayList<>();
        allErrors.addAll(rowErrors);
        allErrors.addAll(colErrors);
        allErrors.addAll(boxErrors);
        
        boolean isValid = allErrors.isEmpty();
        return new VerificationResult(isValid, allErrors);
    }
    
    public List<ValidationError> validateRows(int[][] board) {
        List<ValidationError> errors = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            ValidationError error = rowValidator.validate(board, i);
            if (error != null) {
                errors.add(error);
            }
        }
        return errors;
    }
    
    public List<ValidationError> validateColumns(int[][] board) {
        List<ValidationError> errors = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            ValidationError error = colValidator.validate(board, i);
            if (error != null) {
                errors.add(error);
            }
        }
        return errors;
    }
    
    public List<ValidationError> validateBoxes(int[][] board) {
        List<ValidationError> errors = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            ValidationError error = boxValidator.validate(board, i);
            if (error != null) {
                errors.add(error);
            }
        }
        return errors;
    }
}
