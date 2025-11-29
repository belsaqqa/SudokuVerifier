package sudoku;

import java.util.*;
import java.util.concurrent.*;

public class TwentySevenThreadVerifier implements VerifierStrategy {
    private final ExecutorService executor;
    private NumberValidator numberValidator;
    
    public TwentySevenThreadVerifier() {
        this.executor = Executors.newFixedThreadPool(27);
        this.numberValidator = new CompleteSetValidator();
    }
    
    @Override
    public VerificationResult verify(int[][] board) {
        List<ValidationError> allErrors = Collections.synchronizedList(new ArrayList<>());
        List<Callable<Void>> tasks = createAllValidationTasks(board, allErrors);
        
        try {
            // Launch all 27 workers simultaneously
            List<Future<Void>> futures = executor.invokeAll(tasks);
            
            // Wait for all tasks to complete
            for (Future<Void> future : futures) {
                future.get(); // Just wait for completion, we don't need return values
            }
            
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Parallel verification failed", e);
        } finally {
            executor.shutdown();
        }
        
        // Create the final result
        boolean isValid = allErrors.isEmpty();
        return new VerificationResult(isValid, allErrors);
    }
    
    private List<Callable<Void>> createAllValidationTasks(int[][] board, List<ValidationError> allErrors) {
        List<Callable<Void>> tasks = new ArrayList<>();
        
        // Create 9 row validators (indices 0-8)
        for (int i = 0; i < 9; i++) {
            tasks.add(new ValidationWorker(new RowVerifier(numberValidator), i, board, allErrors));
        }
        
        // Create 9 column validators (indices 0-8)
        for (int i = 0; i < 9; i++) {
            tasks.add(new ValidationWorker(new ColumnVerifier(numberValidator), i, board, allErrors));
        }
        
        // Create 9 box validators (indices 1-9 for BoxVerifier)
        for (int i = 1; i <= 9; i++) {
            tasks.add(new ValidationWorker(new BoxVerifier(numberValidator), i, board, allErrors));
        }
        
        return tasks;
    }
}